package org.example.user

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.example.constants.AppConstants
import org.example.util.AlgoLogger
import org.example.util.FileResource
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.util.Map


interface Commands {
    fun writeToConsole(string: String)
    fun readFromConsole() : String
}

val singleModule = module {
    single { UserState() }
}

var app = startKoin {
    modules(singleModule)
}



class UserOptions : Commands {


    private val state by inject<UserState>(UserState::class.java)
    private val userFileData by inject<FileResource>(FileResource::class.java)
    private val logger by inject<AlgoLogger>(AlgoLogger::class.java)

    private val mapOfOptions = mutableMapOf<String, String>()

    override fun writeToConsole(string: String) {
        logger.log_info(string)
    }



    override fun readFromConsole(): String {
        val string = readln()
        logger.log_info("You entered : $string")
        return string
    }


    fun checkPreviousLogin() = userFileData.getData().let {
        if (it == null) false else {
            state.updateOptions(it.map{ Pair(it.key, it.value.toString()) }.toMap().toMutableMap())
            true
        }
    }

    fun loginFlowBegin() : Boolean {

        println("Please login :")

        writeToConsole(AppConstants.APP_SETUP_BINANCE_API_KEY)
        mapOfOptions[AppConstants.APP_SETUP_BINANCE_API_KEY] = readFromConsole()

        writeToConsole(AppConstants.APP_SETUP_API_BINANCE_SECRET)
        mapOfOptions[AppConstants.APP_SETUP_API_BINANCE_SECRET] = readFromConsole()

        writeToConsole(AppConstants.APP_SETUP_TAAPI_API_KEY)
        mapOfOptions[AppConstants.APP_SETUP_TAAPI_API_KEY] = readFromConsole()

        writeToConsole(AppConstants.APP_SETUP_API_TAAPI_SECRET)
        mapOfOptions[AppConstants.APP_SETUP_API_TAAPI_SECRET] = readFromConsole()


        state.updateOptions(mapOfOptions)
        userFileData.setData(mapOfOptions)

        return mapOfOptions.size == 7

    }
}
