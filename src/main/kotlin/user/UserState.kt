package org.example.user

import kotlinx.coroutines.flow.*

class UserState {

    // StateFlow holds true or false if user is authenticated
    private val isAuthenticated = MutableStateFlow(false)

    // StateFlow holds user setup info
    private val options = MutableStateFlow(emptyMap<String, String>().toMutableMap())

    // Public read-only access
    val _isAuthenticated: StateFlow<Boolean> = isAuthenticated
    val _options: StateFlow<MutableMap<String, String>> = options


    fun updateAuthStatus(isAuthenticated: Boolean) {
        this.isAuthenticated.value = isAuthenticated
    }

    fun updateOptions(newOptions: MutableMap<String, String>) {
        options.update { currentOptions ->
            //val map = newOptions.map{ Pair(it.key, it.value.toString()) }
            currentOptions.putAll(newOptions)
            currentOptions
        }
    }

}