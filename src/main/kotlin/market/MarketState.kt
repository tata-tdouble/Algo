package org.example.market

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MarketState {

    // StateFlow holds default]
    private val ema_1 = MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val ema_5 = MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val ema_23 = MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val ema_80 = MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val ema_200 = MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val vk_rsi =MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))
    private val vd_rsi =MutableStateFlow(ArrayDeque(listOf(0.0, 0.0, 0.0)))

    // Public read-only access
    val _ema_1: StateFlow<ArrayDeque<Double>> = ema_1
    val _ema_5: StateFlow<ArrayDeque<Double>> = ema_5
    val _ema_23: StateFlow<ArrayDeque<Double>> = ema_23
    val _ema_80: StateFlow<ArrayDeque<Double>> = ema_80
    val _ema_200: StateFlow<ArrayDeque<Double>> = ema_200
    val _vk_rsi: StateFlow<ArrayDeque<Double>> = vk_rsi
    val _vd_rsi: StateFlow<ArrayDeque<Double>> = vd_rsi

    fun printMarketState(){
        println("*** MS ***")
        println(ema_1.value)
        println(ema_5.value)
        println(ema_23.value)
        println(ema_80.value)
        println(ema_200.value)
        println(vk_rsi.value)
        println(vd_rsi.value)
        println("--- MS ---")
    }

    fun updateEMA1(double: Double) {
        this.ema_1.value.addFirst(double)
        if (this.ema_1.value.size > 500) this.ema_1.value.removeLast()
    }

    fun updateEMA5(double: Double) {
        this.ema_5.value.addFirst(double)
        if (this.ema_5.value.size > 35) this.ema_5.value.removeLast()
    }

    fun updateEMA23(double: Double) {
        this.ema_23.value.addFirst(double)
        if (this.ema_23.value.size > 100) this.ema_23.value.removeLast()
    }

    fun updateEMA80(double: Double) {
        this.ema_80.value.addFirst(double)
        if (this.ema_80.value.size > 200) this.ema_80.value.removeLast()
    }

    fun updateEMA200(double: Double) {
        this.ema_200.value.addFirst(double)
        if (this.ema_200.value.size > 500) this.ema_200.value.removeLast()
    }

    fun updateVKRSI(double: Double) {
        this.vk_rsi.value.addFirst(double)
        if (this.vk_rsi.value.size > 3) this.vk_rsi.value.removeLast()
    }

    fun updateVDRSI(double: Double) {
        this.vd_rsi.value.addFirst(double)
        if (this.vd_rsi.value.size > 3) this.vd_rsi.value.removeLast()
    }

}