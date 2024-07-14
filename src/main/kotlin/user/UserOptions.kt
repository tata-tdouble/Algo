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

        mapOfOptions[AppConstants.APP_SETUP_BINANCE_API_KEY] = "pLW2Fh180QFWHCE41tTokNCTltHDNpbFuFhSDhJuMAiKLk1EHUAfd7PLwmRycGYh" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_API_BINANCE_SECRET] = "aPeiV0oGCaeVLzvs2N5Oowvi8VKdCDp363Hm4wWu3znfWBDqSLR1vQqw2yF07RXJ" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_TAAPI_API_KEY] = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbHVlIjoiNjY1MjE1NDlmNWFmOTRlZWNlODlkNGMzIiwiaWF0IjoxNzE3Njg3OTk1LCJleHAiOjMzMjIyMTUxOTk1fQ.EGf4h2c43gM_Wzdcg2neXmsTu2IyTeNjhl5ppNyqH04" //readFromConsole()
        mapOfOptions[AppConstants.APP_SETUP_API_TAAPI_SECRET] = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbHVlIjoiNjY1MjE1NDlmNWFmOTRlZWNlODlkNGMzIiwiaWF0IjoxNzE3Njg3OTk1LCJleHAiOjMzMjIyMTUxOTk1fQ.EGf4h2c43gM_Wzdcg2neXmsTu2IyTeNjhl5ppNyqH04" //readFromConsole()

        state.updateOptions(mapOfOptions)

        return mapOfOptions.size == 7

    }
}
