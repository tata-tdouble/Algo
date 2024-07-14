package org.example.market

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.example.constants.AppConstants.APP_SETUP_API_TAAPI_SECRET
import org.example.constants.AppConstants.BASE_URL
import org.example.constants.AppConstants.ema_1_0_data
import org.example.constants.AppConstants.ema_200_0_data
import org.example.constants.AppConstants.ema_23_0_data
import org.example.constants.AppConstants.ema_5_0_data
import org.example.constants.AppConstants.ema_80_0_data
import org.example.constants.AppConstants.valueFastD_rsi
import org.example.constants.AppConstants.valueFastK_rsi
import org.example.user.UserState
import org.koin.java.KoinJavaComponent.inject

class Network {

    private val marketState by inject<MarketState>(MarketState::class.java)
    private val userState by inject<UserState>(UserState::class.java)

    suspend fun loadEMAData() = coroutineScope {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        val obj = buildJsonObject {
            put("secret", userState._options.value[APP_SETUP_API_TAAPI_SECRET])
            putJsonArray("construct") {
                addJsonObject {
                    put("exchange", "binance")
                    put("symbol", "BTC/USDT")
                    put("interval", "1m")
                    putJsonArray("indicators") {
                        addJsonObject { put("indicator", "ema"); put("period", 1) }
                        addJsonObject { put("indicator", "ema"); put("period", 5) }
                        addJsonObject { put("indicator", "ema"); put("period", 23) }
                        addJsonObject { put("indicator", "ema"); put("period", 80) }
                        addJsonObject { put("indicator", "ema"); put("period", 200) }
                    }
                }
            }
        }

        val result = client.use {
            it.post(BASE_URL) {
                contentType(ContentType.Application.Json)
                setBody(obj)
                onDownload { sent, received ->
                    //logger.log_trace("EMA Data Downloaded")
                }
            }
        }

        val responseBody: String = result.bodyAsText()
        extractEMAValues(responseBody)
    }

    suspend fun loadRsiData() = coroutineScope {
        val url = "https://api.taapi.io/stochrsi?secret=${userState._options.value[APP_SETUP_API_TAAPI_SECRET]}&exchange=binance&symbol=BTC/USDT&interval=1m&chart=heikinashi"
        val client = HttpClient(CIO)

        try {
            val response = client.get(url)
            if (response.status.isSuccess()) {
                val data = response.bodyAsText()
                try {
                    val gson = Gson()
                    val responseObject = gson.fromJson(data, RsiValues::class.java)
                    val map = mapOf(
                        valueFastK_rsi to responseObject.valueFastK,
                        valueFastD_rsi to responseObject.valueFastD
                    )
                    extractRSIValues(map)
                //    logger.log_trace("RSI Data Downloaded")
                    //marketState.printMarketState()
                } catch (e: JsonParseException) {
                    println("Error parsing JSON: ${e.message}")
                }
            } else {
                println("Error fetching data: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            client.close()
        }
    }

    private fun extractEMAValues(jsonString: String) {
        val gson = Gson()
        val dataElement = gson.fromJson(jsonString, JsonElement::class.java).asJsonObject
        val jsonArray = dataElement["data"].asJsonArray

        for (item in jsonArray) {
            val obj = item.asJsonObject
            val id = obj["id"]?.asString ?: continue
            val resultObject = obj["result"]?.asJsonObject ?: continue
            val value = resultObject["value"]?.asDouble ?: continue

            when (id) {
                ema_1_0_data -> marketState.updateEMA1(value)
                ema_5_0_data -> marketState.updateEMA5(value)
                ema_23_0_data -> marketState.updateEMA23(value)
                ema_80_0_data -> marketState.updateEMA80(value)
                ema_200_0_data -> marketState.updateEMA200(value)
            }
        }
    }

    private fun extractRSIValues(map: Map<String, Double>) {
        for ((key, value) in map) {
            when (key) {
                valueFastK_rsi -> marketState.updateVKRSI(value)
                valueFastD_rsi -> marketState.updateVDRSI(value)
            }
        }
    }

}

data class RsiValues(val valueFastK: Double, val valueFastD: Double)
