package org.example.user

import org.example.constants.AppConstants
import org.koin.java.KoinJavaComponent.inject



interface Commands {
    fun writeToConsole(string: String)
}



class UserOptions : Commands {


    private val state by inject<UserState>(UserState::class.java)

    private val mapOfOptions = mutableMapOf<String, String>()

    override fun writeToConsole(string: String) {
        println(string)
    }



    fun loginFlowBegin() : Boolean {

        mapOfOptions[AppConstants.APP_SETUP_BINANCE_API_KEY] = "" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_API_BINANCE_SECRET] = "" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_TAAPI_API_KEY] = "" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_API_TAAPI_SECRET] = "" //readFromConsole()

        state.updateOptions(mapOfOptions)

        return mapOfOptions.size == 4

    }
}
