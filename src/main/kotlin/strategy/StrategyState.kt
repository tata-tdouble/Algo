package org.example.strategy


import kotlinx.coroutines.flow.*
import org.example.market.research.Prediction
import org.example.trade.TradeState
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

class StrategyState {

    private val tradeState by inject<TradeState>(TradeState::class.java)

    // StateFlow holds true or false if user is authenticated
    private val _entryState = MutableStateFlow(ArrayList<Int>())
    private val _exitState = MutableStateFlow(ArrayList<Int>())
    private val _position = MutableStateFlow(ArrayList<Int>())

    // Public read-only access
    val entryState : StateFlow<ArrayList<Int>> = _entryState
    val exitState : StateFlow<ArrayList<Int>> = _exitState
    val position : StateFlow<ArrayList<Int>> = _position

    private val strategy by inject<Strategy>(Strategy::class.java)

    private val entryStrategy_1 = MutableStateFlow(false)
    private val entryStrategy_2 = MutableStateFlow(false)
    private val entryStrategy_3 = MutableStateFlow(false)
    private val entryStrategy_4 = MutableStateFlow(false)
    private val entryStrategy_5 = MutableStateFlow(false)
    private val entryStrategy_6 = MutableStateFlow(false)
    private val entryStrategy_7 = MutableStateFlow(false)
    private val entryStrategy_8 = MutableStateFlow(false)

    private val exitStrategy_1 = MutableStateFlow(false)
    private val exitStrategy_2 = MutableStateFlow(false)
    private val exitStrategy_3 = MutableStateFlow(false)
    private val exitStrategy_4 = MutableStateFlow(false)
    private val exitStrategy_5 = MutableStateFlow(false)
    private val exitStrategy_6 = MutableStateFlow(false)
    private val exitStrategy_7 = MutableStateFlow(false)

    fun update(){

            entryStrategy_1.value = strategy.entry_calculate_uptrend()
            entryStrategy_2.value = strategy.entry_calculate_overall_weight()
            entryStrategy_3.value = strategy.entry_calculate_overall_positive_gradient()
            entryStrategy_4.value = strategy.entry_disparity_of_ema()
            entryStrategy_5.value = strategy.entry_stock_rsi_1()
            entryStrategy_6.value = strategy.entry_stock_rsi_2()
            entryStrategy_7.value = strategy.entry_calculate_high_point_angle()
            entryStrategy_6.value = strategy.entry_calculate_line_cut_from_under()

            exitStrategy_1.value = strategy.exit_calculate_overall_weight()
            exitStrategy_2.value = strategy.exit_disparity_of_ema()
            exitStrategy_3.value = strategy.exit_stock_rsi_1()
            exitStrategy_4.value = strategy.exit_stock_rsi_2()
            exitStrategy_5.value = strategy.exit_stop_loss()
            exitStrategy_6.value = strategy.exit_calculate_high_point_angle()
            exitStrategy_7.value = strategy.exit_calculate_high_point_value()

            calc_state()
    }

    fun printStrategyState(){
        println("*** Entry S ***")
        println("e1 "+entryStrategy_1.value)
        println("e2 "+entryStrategy_2.value)
        println("e3 "+entryStrategy_3.value)
        println("e4 "+entryStrategy_4.value)
        println("e5 "+entryStrategy_5.value)
        println("e6 "+entryStrategy_6.value)
        println("e6 "+entryStrategy_7.value)
        println("e6 "+entryStrategy_8.value)
        println("--- ES ---")

        println("*** Exit S ***")
        println("f1 "+exitStrategy_1.value)
        println("f2 "+exitStrategy_2.value)
        println("f3 "+exitStrategy_3.value)
        println("f4 "+exitStrategy_4.value)
        println("f5 "+exitStrategy_5.value)
        println("f5 "+exitStrategy_6.value)
        println("f5 "+exitStrategy_7.value)
        println("--- ES ---")
    }

    fun calc_state() {

        val e1 = if (entryStrategy_1.value) 2 else if (entryStrategy_3.value)  1 else 0
        val e2 = if (entryStrategy_2.value) 1 else 0
        val e3 = if (entryStrategy_3.value) 2 else 0
        val e4 = if (entryStrategy_4.value) 1 else 0
        val e5 = if (entryStrategy_5.value || entryStrategy_6.value) 2 else -1
        val e6 = if (entryStrategy_5.value && entryStrategy_6.value) 3 else if (entryStrategy_6.value)  -3 else 0
        val e7 = if (entryStrategy_7.value) 1 else 0
        val e8 = if (entryStrategy_8.value) 1 else 0

        _entryState.value.add(0, e1 + e2 + e3 + e4 + e5 + e6 + e7 + e8)
        if (_entryState.value.size > 200) _entryState.value.removeLast()

        val f1 = if (exitStrategy_1.value) 1 else 0
        val f2 = if (exitStrategy_2.value) 1 else 0
        val f3 = if (exitStrategy_3.value || exitStrategy_4.value) 2 else -2
        val f4 = if (exitStrategy_3.value && exitStrategy_4.value) 4 else if (entryStrategy_3.value)  -3 else 0
        val f5 = if (exitStrategy_5.value) 1 else 0
        val f6 = if (exitStrategy_6.value) 1 else 0
        val f7 = if (exitStrategy_7.value) 1 else 0

        _exitState.value.add(0, f1 + f2 + f3 + f4 + f5 + f6 + f7)
        if (_exitState.value.size > 200) _exitState.value.removeLast()

        _position.value.add(0, (e1+e2+e3+e4+e5+e6+e7+e8) - (f1+f2+f3+f4+f5+f6+f7) )
    }

    fun generate_signal(prediction: Prediction): Signal {
        return if (tradeState._inTheMarket.value)
            search_for_bearish_signal(prediction)
        else
            search_for_bullish_signal(prediction)
    }

    fun search_for_bullish_signal(prediction: Prediction): Signal {
        val market_1 = check_for_bearish_market_ema5(prediction)
        val market_2 = check_for_bearish_market_ema23(prediction)
        val market_3 = check_for_bearish_market_ema80(prediction)
        val market_4 = check_for_bearish_market_ema200(prediction)

        if (!market_1) return Signal(0, 0.0)
        if (!market_2) return Signal(0, 0.0)
        if (!market_3) return Signal(0, 0.0)
        if (!market_4) return Signal(0, 0.0)

        return Signal(1, prediction.median_bearish_price_percentage_ema80_1!!)
    }

    fun search_for_bearish_signal(prediction: Prediction): Signal {
        val market_1 = check_for_bullish_market_ema5(prediction)
        val market_2 = check_for_bullish_market_ema23(prediction)
        val market_3 = check_for_bullish_market_ema80(prediction)
        val market_4 = check_for_bullish_market_ema200(prediction)

        if (!market_1) return Signal(0, 0.0)
        if (!market_2) return Signal(0, 0.0)
        if (!market_3) return Signal(0, 0.0)
        if (!market_4) return Signal(0, 0.0)

        return Signal(-1, prediction.median_bullish_price_percentage_ema80_1!!)
    }

    fun check_for_bullish_market_ema5(prediction: Prediction): Boolean {
        val ema5_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bullish_time_ema5.roundToInt()).toList()) ?: 0.0
        val ema5_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema5_1!!).toList()) ?: 0.0
        val ema5_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema5_2!!).toList()) ?: 0.0
        return ema5_market_time_1 > 1.0 || ema5_market_time_2 > 1.0 || ema5_market_time_3 > 1.0
    }

    fun check_for_bearish_market_ema5(prediction: Prediction): Boolean {
        val ema5_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bearish_time_ema5.roundToInt()).toList()) ?: 0.0
        val ema5_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema5_1!!).toList()) ?: 0.0
        val ema5_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema5_2!!).toList()) ?: 0.0
        return ema5_market_time_1 < -1.0 || ema5_market_time_2 < -1.0 || ema5_market_time_3 < -1.0
    }

    fun check_for_bullish_market_ema23(prediction: Prediction): Boolean {
        val ema23_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bullish_time_ema23.roundToInt()).toList())
        val ema23_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema23_1!!).toList())
        val ema23_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema23_2!!).toList())
        return ema23_market_time_1!! > 0.5 || ema23_market_time_2!! > 0.5 || ema23_market_time_3!! > 0.5
    }

    fun check_for_bearish_market_ema23(prediction: Prediction): Boolean {
        val ema23_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bearish_time_ema23.roundToInt()).toList())
        val ema23_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema23_1!!).toList())
        val ema23_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema23_2!!).toList())
        return ema23_market_time_1!! < -0.5 || ema23_market_time_2!! < -0.5 || ema23_market_time_3!! < -0.5
    }

    fun check_for_bullish_market_ema80(prediction: Prediction): Boolean {
        val ema80_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bullish_time_ema80.roundToInt()).toList())
        val ema80_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema80_1!!).toList())
        val ema80_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema80_2!!).toList())
        return ema80_market_time_1!! > 0.0 || ema80_market_time_2!! > 0.0 || ema80_market_time_3!! > 0.0
    }

    fun check_for_bearish_market_ema80(prediction: Prediction): Boolean {
        val ema80_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bearish_time_ema80.roundToInt()).toList())
        val ema80_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema80_1!!).toList())
        val ema80_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema80_2!!).toList())
        return ema80_market_time_1!! < 0 || ema80_market_time_2!! < 0 || ema80_market_time_3!! < 0
    }

    fun check_for_bullish_market_ema200(prediction: Prediction): Boolean {
        val ema200_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bullish_time_ema200.roundToInt()).toList())
        val ema200_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema200_1!!).toList())
        val ema200_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bullish_time_ema200_2!!).toList())
        return ema200_market_time_1!! > 0.0 || ema200_market_time_2!! > 0.0 || ema200_market_time_3!! > 0.0
    }

    fun check_for_bearish_market_ema200(prediction: Prediction): Boolean {
        val ema200_market_time_1 = calculateGradient(_position.value.subList(0, prediction.average_bearish_time_ema200.roundToInt()).toList())
        val ema200_market_time_2 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema200_1!!).toList())
        val ema200_market_time_3 = calculateGradient(_position.value.subList(0, prediction.median_bearish_time_ema200_2!!).toList())
        return ema200_market_time_1!! < 0 || ema200_market_time_2!! < 0 || ema200_market_time_3!! < 0
    }

    fun calculateGradient(data: List<Int>): Double? {
        if (data.size < 2) {
            return null // Gradient cannot be calculated with less than 2 points
        }

        val dx = 1.0 // Assuming constant distance between points (adjust if needed)
        var sumY = 0.0

        for (i in 1 until data.size) {
            val dy = data[i] - data[i - 1]
            sumY += dy
        }

        val gradient = sumY / (dx * (data.size - 1))
        return gradient
    }

    data class Signal(
        val int: Int,
        val percentage: Double
    )

}
