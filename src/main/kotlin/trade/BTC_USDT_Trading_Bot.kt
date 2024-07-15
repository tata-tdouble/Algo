package org.example.trade

import com.binance.connector.client.SpotClient
import com.binance.connector.client.impl.SpotClientImpl
import com.binance.connector.client.utils.signaturegenerator.HmacSignatureGenerator
import org.example.constants.AppConstants.APP_SETUP_API_BINANCE_SECRET
import org.example.constants.AppConstants.APP_SETUP_BINANCE_API_KEY
import org.example.strategy.StrategyState
import org.example.user.UserState
import org.koin.java.KoinJavaComponent.inject

class BTC_USDT_Trading_Bot {

    private val strategyState by inject<StrategyState>(StrategyState::class.java)
    private val userState by inject<UserState>(UserState::class.java)

    var signGenerator: HmacSignatureGenerator = HmacSignatureGenerator(userState._options.value.get(APP_SETUP_API_BINANCE_SECRET))

    var client: SpotClient = SpotClientImpl(
        userState._options.value.get(APP_SETUP_BINANCE_API_KEY),
        signGenerator,
        "https://api-gcp.binance.com"
    )

    fun entry_listener_exec() {
        val mEntryState = strategyState.entryState.value
        //if (tradeState._inTheMarket.value && it > 6) {
        //bearish_action()
        //tradeState.updateState(false)

        println("bull" + mEntryState.toString())
        //}
    }

    fun exit_listener_exec() {
        val mExitState = strategyState.exitState.value
        //if (tradeState._inTheMarket.value && it > 6) {
        //bearish_action()
        //tradeState.updateState(false)

        println("bear" + mExitState.toString())
        //}
    }

    fun printResult() {
        val mExitState = strategyState.exitState.value
        val mEntryState = strategyState.entryState.value
        //if (tradeState._inTheMarket.value && it > 6) {
        //bearish_action()
        //tradeState.updateState(false)

        println("Position : ${mEntryState - mExitState}")
        //}
    }

    private fun bullish_action(){
        val parameters: MutableMap<String, Any> = LinkedHashMap()
        parameters["symbol"] = "BTCUSDT"
        parameters["side"] = "BUY"
        parameters["type"] = "LIMIT"
        parameters["timeInForce"] = "GTC"
        parameters["quantity"] = 0.01
        parameters["price"] = 9500

        val result = client.createTrade().testNewOrder(parameters)
    }

    private fun bearish_action(){
        val parameters: MutableMap<String, Any> = LinkedHashMap()
        parameters["symbol"] = "BTCUSDT"
        parameters["side"] = "SELL"
        parameters["type"] = "LIMIT"
        parameters["timeInForce"] = "GTC"
        parameters["quantity"] = 0.01
        parameters["price"] = 9500

        val result = client.createTrade().testNewOrder(parameters)
    }

}