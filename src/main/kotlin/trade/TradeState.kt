package org.example.trade

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TradeState {

    private val inTheMarket = MutableStateFlow(false)
    val _inTheMarket : StateFlow<Boolean> = inTheMarket

    fun updateState (value : Boolean){
        inTheMarket.value = value
    }
}