package org.example.auth

import kotlinx.coroutines.flow.onEach
import org.example.market.Controller
import org.example.user.UserOptions
import org.example.user.UserState
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Logger
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class AuthManager {

    private val state by inject<UserState>(UserState::class.java)
    private val options by inject<UserOptions>(UserOptions::class.java)
    private val controller by inject<Controller>(Controller::class.java)

    suspend fun startTrade() {
        loginFlow()
        println("Starting Bot ...")
        controller.takeControl()
    }

    fun loginFlow(): Boolean {
        return options.loginFlowBegin()
    }

    fun signOut() {
        state.updateAuthStatus(false)
    }

}