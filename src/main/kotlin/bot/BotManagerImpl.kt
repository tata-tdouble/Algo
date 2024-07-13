package org.example.bot

import org.example.auth.AuthManager
import org.example.di.applicationModules
import org.example.user.UserOptions
import org.example.user.UserState
import org.example.util.AlgoLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

import org.koin.java.KoinJavaComponent.inject

class BotManagerImpl: BotManager {


    var app = startKoin {
        modules(listOf(applicationModules))
    }


    private val state by inject<UserState>(UserState::class.java)
    private val auth by inject<AuthManager>(AuthManager::class.java)
    private val logger by inject<AlgoLogger>(AlgoLogger::class.java)




    override suspend fun startBot() {
        // In your Activity/Fragment/Composable (where you want to listen)

        state._isAuthenticated.collect { isAuth ->
            // Update UI based on the new authentication state (e.g., show login/logout button)
            if (isAuth) {
                auth.startTrade()
            } else {
                if(auth.loginFlow()){
                    auth.startTrade()
                } else {
                    stopBot()
                }
            }
        }

    }

    override fun stopBot() {
        logger.log_warn("Stopping bot !!")
        auth.signOut()
    }
}