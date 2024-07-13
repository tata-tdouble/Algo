package org.example.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.inject
import java.io.BufferedReader
import java.io.File

class FileResource {

    private val my_data = File("my_data.dat") // Custom location or use temporary directory


    private val logger by inject<AlgoLogger>(AlgoLogger::class.java)


    fun setData(dataMap: Map<String, Any>) {
        val dataString = Gson().toJson(dataMap)
        my_data.writeText(dataString)
    }

    fun setMarketData(dataMap: String, fileName: String) {
        val data = File(fileName) // Custom location or use temporary directory
        val dataString = Gson().toJson(dataMap)
        data.writeText(dataString)
    }




    fun getData(): Map<String, Any>? {
        if (!my_data.exists()) {
            return null // No data file, no data
        }
        val dataString = my_data.readText()
        return try {
            val gson = Gson()
            val type = object : TypeToken<Map<String, Any>>(){}.type
            val map : Map<String, Any> = gson.fromJson(dataString, type)
            map
        } catch (e: Exception) {
            logger.log_err("Error getting user info")
            logger.log_err("Messade: ${e.message}")
            null
        }
    }

    fun getMarketData(file: File): String? {

        if (!file.exists()) {
            return null // No data file, no data
        }

        val lines = try {
            val reader = BufferedReader(file.reader())
            val firstThreeLines = reader.useLines { it.take(3).toList() }
            reader.close()
            firstThreeLines
        } catch (e: Exception) {
            logger.log_err("Error reading RSI data file")
            logger.log_err("Message: ${e.message}")
            return null
        }

        // Combine the first three lines into a single JSON string
        val dataString = lines.joinToString("")

        return try {
            val gson = Gson()
            val type = object : TypeToken<String>() {}.type
            val map: String = gson.fromJson(dataString, type)
            map
        } catch (e: Exception) {
            logger.log_err("Error parsing RSI data")
            logger.log_err("Message: ${e.message}")
            null
        }
    }

}