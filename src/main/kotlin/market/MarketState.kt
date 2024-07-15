package org.example.market

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.market.research.EpochLevel
import org.example.util.generateDoubleList
import kotlin.properties.Delegates

class MarketState {

    // StateFlow holds default]
    private val ema_1 = MutableStateFlow(ArrayDeque(generateDoubleList(500, (40.00..75.00))))
    private val ema_5 = MutableStateFlow(ArrayDeque(generateDoubleList(35, (40.00..75.00))))
    private val ema_23 = MutableStateFlow(ArrayDeque(generateDoubleList(100, (40.00..75.00))))
    private val ema_80 = MutableStateFlow(ArrayDeque(generateDoubleList(200, (40.00..75.00))))
    private val ema_200 = MutableStateFlow(ArrayDeque(generateDoubleList(500, (40.00..75.00))))
    private val vk_rsi =MutableStateFlow(ArrayDeque(generateDoubleList(3, (0.00..100.00))))
    private val vd_rsi =MutableStateFlow(ArrayDeque(generateDoubleList(2, (0.00..100.00))))

    // Public read-only access
    val _ema_1: StateFlow<ArrayDeque<Double>> = ema_1
    val _ema_5: StateFlow<ArrayDeque<Double>> = ema_5
    val _ema_23: StateFlow<ArrayDeque<Double>> = ema_23
    val _ema_80: StateFlow<ArrayDeque<Double>> = ema_80
    val _ema_200: StateFlow<ArrayDeque<Double>> = ema_200
    val _vk_rsi: StateFlow<ArrayDeque<Double>> = vk_rsi
    val _vd_rsi: StateFlow<ArrayDeque<Double>> = vd_rsi

    var emaState : MutableList<EmaState> = mutableListOf()

    var previousPositiveLevels : MutableList<EpochLevel> = mutableListOf()
    var previousNegativeLevels : MutableList<EpochLevel> = mutableListOf()

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

    fun calc_ema_state() {
        val state = EmaState()
        state.ema5 = _ema_1.value.first() > _ema_5.value.first()
        state.ema23 = _ema_1.value.first() > _ema_23.value.first()
        state.ema80 = _ema_1.value.first() > _ema_80.value.first()
        state.ema200 = _ema_1.value.first() > _ema_200.value.first()
        emaState.add(0, state)
        if(emaState.size > 2) emaState.removeLast()
    }

    class EmaState{
        var ema5 by Delegates.notNull<Boolean>()
        var ema23 by Delegates.notNull<Boolean>()
        var ema80 by Delegates.notNull<Boolean>()
        var ema200 by Delegates.notNull<Boolean>()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is EmaState) return false
            return ema5 == other.ema5 &&
                    ema23 == other.ema23 &&
                    ema80 == other.ema80 &&
                    ema200 == other.ema200
        }
    }

}