package org.example.market.ai

import Predictor
import org.example.market.MarketState
import org.example.market.research.DataProcessor
import org.example.market.research.Epoch
import org.example.market.research.EpochLevel
import org.koin.java.KoinJavaComponent.inject
import java.io.*

class Trainer {

    private val dataProcessor by inject<DataProcessor>(DataProcessor::class.java)
    private val marketState by inject<MarketState>(MarketState::class.java)

    fun counter() {
        if (!marketState.emaState.first().equals(marketState.emaState.last())) {

            val ema1 = marketState._ema_1.value.first()
            val ema5 = marketState._ema_5.value.first()
            val ema23 = marketState._ema_23.value.first()
            val ema80 = marketState._ema_80.value.first()
            val ema200 = marketState._ema_200.value.first()

            val positiveLevels = mutableListOf<EpochLevel>()
            val negativeLevels = mutableListOf<EpochLevel>()

            if (ema1 > ema5) positiveLevels.add(EpochLevel.EMA5) else negativeLevels.add(EpochLevel.EMA5)
            if (ema1 > ema23) positiveLevels.add(EpochLevel.EMA23) else negativeLevels.add(EpochLevel.EMA23)
            if (ema1 > ema80) positiveLevels.add(EpochLevel.EMA80) else negativeLevels.add(EpochLevel.EMA80)
            if (ema1 > ema200) positiveLevels.add(EpochLevel.EMA200) else negativeLevels.add(EpochLevel.EMA200)

            // Check for crossing
            val previousPositiveLevels = marketState.previousPositiveLevels // Assuming you have previous levels stored
            val previousNegativeLevels = marketState.previousNegativeLevels // Assuming you have previous levels stored

            val newCrossingLevel = when {
                previousPositiveLevels.contains(EpochLevel.EMA200) && negativeLevels.contains(EpochLevel.EMA200) -> Pair(
                    true,
                    EpochLevel.EMA200
                )

                previousPositiveLevels.contains(EpochLevel.EMA80) && negativeLevels.contains(EpochLevel.EMA80) -> Pair(
                    true,
                    EpochLevel.EMA80
                )

                previousPositiveLevels.contains(EpochLevel.EMA23) && negativeLevels.contains(EpochLevel.EMA23) -> Pair(
                    true,
                    EpochLevel.EMA23
                )

                previousPositiveLevels.contains(EpochLevel.EMA5) && negativeLevels.contains(EpochLevel.EMA5) -> Pair(
                    true,
                    EpochLevel.EMA5
                )

                previousNegativeLevels.contains(EpochLevel.EMA200) && positiveLevels.contains(EpochLevel.EMA200) -> Pair(
                    false,
                    EpochLevel.EMA200
                )

                previousNegativeLevels.contains(EpochLevel.EMA80) && positiveLevels.contains(EpochLevel.EMA80) -> Pair(
                    false,
                    EpochLevel.EMA80
                )

                previousNegativeLevels.contains(EpochLevel.EMA23) && positiveLevels.contains(EpochLevel.EMA23) -> Pair(
                    false,
                    EpochLevel.EMA23
                )

                previousNegativeLevels.contains(EpochLevel.EMA5) && positiveLevels.contains(EpochLevel.EMA5) -> Pair(
                    false,
                    EpochLevel.EMA5
                )

                else -> null
            }

            // Update previous levels
            marketState.previousPositiveLevels = positiveLevels
            marketState.previousNegativeLevels = negativeLevels

            if (newCrossingLevel != null) train(newCrossingLevel)
        }
    }


    fun train(level:  Pair<Boolean, EpochLevel>){
        val normalizer = dataProcessor.getNrmalizer()
        if(level.first) {
            when (level.second) {
                EpochLevel.EMA5 -> {

                    val list = normalizer.get_Bullish_Normalized_EMA5()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_5_BULLISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_5_BULLISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_5_BULLISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_5_BULLISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_5_BULLISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_5_BULLISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_5_BULLISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_5_BULLISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BULLISH_FLOCK_DEC)

                }
                EpochLevel.EMA23 -> {

                    val list = normalizer.get_Bullish_Normalized_EMA23()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_23_BULLISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_23_BULLISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_23_BULLISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_23_BULLISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_23_BULLISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_23_BULLISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_23_BULLISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_23_BULLISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BULLISH_FLOCK_DEC)

                }
                EpochLevel.EMA80 -> {

                    val list = normalizer.get_Bullish_Normalized_EMA80()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_80_BULLISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_80_BULLISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_80_BULLISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_80_BULLISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_80_BULLISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_80_BULLISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_80_BULLISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_80_BULLISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BULLISH_FLOCK_DEC)

                }
                EpochLevel.EMA200 -> {

                    val list = normalizer.get_Bullish_Normalized_EMA200()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_200_BULLISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_200_BULLISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_200_BULLISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_200_BULLISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_200_BULLISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_200_BULLISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_200_BULLISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_200_BULLISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BULLISH_FLOCK_DEC)

                }
            }
        } else {
            when (level.second) {
                EpochLevel.EMA5 -> {

                    val list = normalizer.get_Bearish_Normalized_EMA5()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_5_BEARISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_5_BEARISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_5_BEARISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_5_BEARISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_5_BEARISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_5_BEARISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_5_BEARISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_5_BEARISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_5_BEARISH_FLOCK_DEC)

                }
                EpochLevel.EMA23 -> {

                    val list = normalizer.get_Bearish_Normalized_EMA23()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_23_BEARISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_23_BEARISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_23_BEARISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_23_BEARISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_23_BEARISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_23_BEARISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_23_BEARISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_23_BEARISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_23_BEARISH_FLOCK_DEC)

                }
                EpochLevel.EMA80 -> {

                    val list = normalizer.get_Bearish_Normalized_EMA80()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_80_BEARISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_80_BEARISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_80_BEARISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_80_BEARISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_80_BEARISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_80_BEARISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_80_BEARISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_80_BEARISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_80_BEARISH_FLOCK_DEC)

                }
                EpochLevel.EMA200 -> {

                    val list = normalizer.get_Bearish_Normalized_EMA200()

                    var training_data = list.filter { it.sign == true }.map { it.high }
                    var model = loadModel(EMA_200_BEARISH_HIGH_PRICE)
                    var predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_HIGH_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.low }
                    model = loadModel(EMA_200_BEARISH_LOW_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_LOW_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.entry_price }
                    model = loadModel(EMA_200_BEARISH_ENTRY_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_ENTRY_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.exit_price }
                    model = loadModel(EMA_200_BEARISH_EXIT_PRICE)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_EXIT_PRICE)

                    training_data = list.filter { it.sign == true }.map { it.good_market_time.toDouble() }
                    model = loadModel(EMA_200_BEARISH_GOOD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_GOOD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.bad_market_time.toDouble() }
                    model = loadModel(EMA_200_BEARISH_BAD_TIME)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_BAD_TIME)

                    training_data = list.filter { it.sign == true }.map { it.inc_floctuation_percent }
                    model = loadModel(EMA_200_BEARISH_FLOCK_INC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_FLOCK_INC)

                    training_data = list.filter { it.sign == true }.map { it.dec_floctuation_percent }
                    model = loadModel(EMA_200_BEARISH_FLOCK_DEC)
                    predictor = Predictor(model, training_data)
                    predictor.train(10, training_data.size)
                    saveModel(model, EMA_200_BEARISH_FLOCK_DEC)

                }

            }
        }

    }

    private fun saveModel(model: LstmModel, modelName: String) {
        val modelPath = "ai_models/$modelName" // Model path within JAR
        val outputFile = File(modelPath)
        if (!outputFile.parentFile.exists()) {
            outputFile.parentFile.mkdirs() // Create directories if they don't exist
        }
        val outputStream = FileOutputStream(outputFile)
        val objectOutputStream = ObjectOutputStream(outputStream)
        objectOutputStream.writeObject(model)
        objectOutputStream.close()
        outputStream.close()
    }

    fun loadModel(modelName: String): LstmModel {
        val modelPath = "ai_models/$modelName"
        val inputStream = ClassLoader.getSystemResourceAsStream(modelPath) ?: null
        val loadedModel = if (inputStream == null) {
            LstmModel()
        } else {
            val objectInputStream = ObjectInputStream(inputStream)
            val model = objectInputStream.readObject() as? LstmModel
            objectInputStream.close()
            model
        }
        return loadedModel!!
    }

    companion object{

        const val EMA_5_BULLISH_HIGH_PRICE = "ema_5_bullish_high_price_model"
        const val EMA_5_BULLISH_LOW_PRICE = "ema_5_bullish_low_price_model"
        const val EMA_5_BULLISH_ENTRY_PRICE = "ema_5_bullish_entry_price_model"
        const val EMA_5_BULLISH_EXIT_PRICE = "ema_5_bullish_exit_price_model"
        const val EMA_5_BULLISH_GOOD_TIME = "ema_5_bullish_good_time_model"
        const val EMA_5_BULLISH_BAD_TIME = "ema_5_bullish_bad_time_model"
        const val EMA_5_BULLISH_FLOCK_INC = "ema_5_bullish_flock_inc_model"
        const val EMA_5_BULLISH_FLOCK_DEC = "ema_5_bullish_flock_dec_model"

        const val EMA_23_BULLISH_HIGH_PRICE = "ema_23_bullish_high_price_model"
        const val EMA_23_BULLISH_LOW_PRICE = "ema_23_bullish_low_price_model"
        const val EMA_23_BULLISH_ENTRY_PRICE = "ema_23_bullish_entry_price_model"
        const val EMA_23_BULLISH_EXIT_PRICE = "ema_23_bullish_exit_price_model"
        const val EMA_23_BULLISH_GOOD_TIME = "ema_23_bullish_good_time_model"
        const val EMA_23_BULLISH_BAD_TIME = "ema_23_bullish_bad_time_model"
        const val EMA_23_BULLISH_FLOCK_INC = "ema_23_bullish_flock_inc_model"
        const val EMA_23_BULLISH_FLOCK_DEC = "ema_23_bullish_flock_dec_model"

        const val EMA_80_BULLISH_HIGH_PRICE = "ema_80_bullish_high_price_model"
        const val EMA_80_BULLISH_LOW_PRICE = "ema_80_bullish_low_price_model"
        const val EMA_80_BULLISH_ENTRY_PRICE = "ema_80_bullish_entry_price_model"
        const val EMA_80_BULLISH_EXIT_PRICE = "ema_80_bullish_exit_price_model"
        const val EMA_80_BULLISH_GOOD_TIME = "ema_80_bullish_good_time_model"
        const val EMA_80_BULLISH_BAD_TIME = "ema_80_bullish_bad_time_model"
        const val EMA_80_BULLISH_FLOCK_INC = "ema_80_bullish_flock_inc_model"
        const val EMA_80_BULLISH_FLOCK_DEC = "ema_80_bullish_flock_dec_model"

        const val EMA_200_BULLISH_HIGH_PRICE = "ema_200_bullish_high_price_model"
        const val EMA_200_BULLISH_LOW_PRICE = "ema_200_bullish_low_price_model"
        const val EMA_200_BULLISH_ENTRY_PRICE = "ema_200_bullish_entry_price_model"
        const val EMA_200_BULLISH_EXIT_PRICE = "ema_200_bullish_exit_price_model"
        const val EMA_200_BULLISH_GOOD_TIME = "ema_200_bullish_good_time_model"
        const val EMA_200_BULLISH_BAD_TIME = "ema_200_bullish_bad_time_model"
        const val EMA_200_BULLISH_FLOCK_INC = "ema_200_bullish_flock_inc_model"
        const val EMA_200_BULLISH_FLOCK_DEC = "ema_200_bullish_flock_dec_model"

        const val EMA_5_BEARISH_HIGH_PRICE = "ema_5_bearish_high_price_model"
        const val EMA_5_BEARISH_LOW_PRICE = "ema_5_bearish_low_price_model"
        const val EMA_5_BEARISH_ENTRY_PRICE = "ema_5_bearish_entry_price_model"
        const val EMA_5_BEARISH_EXIT_PRICE = "ema_5_bearish_exit_price_model"
        const val EMA_5_BEARISH_GOOD_TIME = "ema_5_bearish_good_time_model"
        const val EMA_5_BEARISH_BAD_TIME = "ema_5_bearish_bad_time_model"
        const val EMA_5_BEARISH_FLOCK_INC = "ema_5_bearish_flock_inc_model"
        const val EMA_5_BEARISH_FLOCK_DEC = "ema_5_bearish_flock_dec_model"

        const val EMA_23_BEARISH_HIGH_PRICE = "ema_23_bearish_high_price_model"
        const val EMA_23_BEARISH_LOW_PRICE = "ema_23_bearish_low_price_model"
        const val EMA_23_BEARISH_ENTRY_PRICE = "ema_23_bearish_entry_price_model"
        const val EMA_23_BEARISH_EXIT_PRICE = "ema_23_bearish_exit_price_model"
        const val EMA_23_BEARISH_GOOD_TIME = "ema_23_bearish_good_time_model"
        const val EMA_23_BEARISH_BAD_TIME = "ema_23_bearish_bad_time_model"
        const val EMA_23_BEARISH_FLOCK_INC = "ema_23_bearish_flock_inc_model"
        const val EMA_23_BEARISH_FLOCK_DEC = "ema_23_bearish_flock_dec_model"

        const val EMA_80_BEARISH_HIGH_PRICE = "ema_80_bearish_high_price_model"
        const val EMA_80_BEARISH_LOW_PRICE = "ema_80_bearish_low_price_model"
        const val EMA_80_BEARISH_ENTRY_PRICE = "ema_80_bearish_entry_price_model"
        const val EMA_80_BEARISH_EXIT_PRICE = "ema_80_bearish_exit_price_model"
        const val EMA_80_BEARISH_GOOD_TIME = "ema_80_bearish_good_time_model"
        const val EMA_80_BEARISH_BAD_TIME = "ema_80_bearish_bad_time_model"
        const val EMA_80_BEARISH_FLOCK_INC = "ema_80_bearish_flock_inc_model"
        const val EMA_80_BEARISH_FLOCK_DEC = "ema_80_bearish_flock_dec_model"

        const val EMA_200_BEARISH_HIGH_PRICE = "ema_200_bearish_high_price_model"
        const val EMA_200_BEARISH_LOW_PRICE = "ema_200_bearish_low_price_model"
        const val EMA_200_BEARISH_ENTRY_PRICE = "ema_200_bearish_entry_price_model"
        const val EMA_200_BEARISH_EXIT_PRICE = "ema_200_bearish_exit_price_model"
        const val EMA_200_BEARISH_GOOD_TIME = "ema_200_bearish_good_time_model"
        const val EMA_200_BEARISH_BAD_TIME = "ema_200_bearish_bad_time_model"
        const val EMA_200_BEARISH_FLOCK_INC = "ema_200_bearish_flock_inc_model"
        const val EMA_200_BEARISH_FLOCK_DEC = "ema_200_bearish_flock_dec_model"

    }
}