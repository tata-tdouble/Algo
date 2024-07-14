package org.example.bot

import org.example.auth.AuthManager
import org.example.di.applicationModules
import org.example.user.UserState
import org.koin.core.context.GlobalContext.startKoin

import org.koin.java.KoinJavaComponent.inject

class BotManagerImpl: BotManager {


    var app = startKoin {
        modules(listOf(applicationModules))
    }


    private val state by inject<UserState>(UserState::class.java)
    private val auth by inject<AuthManager>(AuthManager::class.java)




    override suspend fun startBot() {
        // In your Activity/Fragment/Composable (where you want to listen)
        auth.startTrade()
    }

    override fun stopBot() {
        println("Stopping bot !!")
        auth.signOut()
    }
}