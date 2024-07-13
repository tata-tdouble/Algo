package org.example.market.research

import org.example.market.MarketState
import org.koin.java.KoinJavaComponent.inject

class DataProcessor {

    private val marketState by inject<MarketState>(MarketState::class.java)

    val epochMap : MutableMap<EpochLevel, List<Epoch>> = mutableMapOf()

    fun predict(): Prediction {
        return Prediction(genEpochData())
    }


    fun genEpochData(): MutableMap<EpochLevel, List<Epoch>>{
        epochMap.clear()
        epochMap.put(EpochLevel.EMA5, splitEMAs(marketState._ema_1.value, marketState._ema_5.value))
        epochMap.put(EpochLevel.EMA23, splitEMAs(marketState._ema_1.value, marketState._ema_23.value))
        epochMap.put(EpochLevel.EMA80, splitEMAs(marketState._ema_1.value, marketState._ema_80.value))
        epochMap.put(EpochLevel.EMA200, splitEMAs(marketState._ema_1.value, marketState._ema_200.value))
        return epochMap
    }


    fun splitEMAs(ema1m: List<Double>, ema_base: List<Double>): List<Epoch> {
        val result = mutableListOf<Epoch>()
        if (ema1m.isEmpty() || ema_base.isEmpty() || ema1m.size != ema_base.size) {
            throw IllegalArgumentException("The EMA lists must be non-empty and of the same length.")
        }

        var startIndex = 0
        var crossingFound = false

        for (i in 1 until ema1m.size) {
            val prevDiff = ema1m[i - 1] - ema_base[i - 1]
            val currentDiff = ema1m[i] - ema_base[i]

            if ((prevDiff <= 0 && currentDiff > 0) || (prevDiff >= 0 && currentDiff < 0)) {
                // Found a crossing
                if (i > startIndex) {
                    val subList = ema1m.subList(startIndex, i)
                    val sign = prevDiff > 0 // sign is true if above, false if below
                    result.add(Epoch(subList, sign))
                }
                startIndex = i
                crossingFound = true
            }
        }

        // Add the remaining part if there was at least one crossing
        if (crossingFound && startIndex < ema1m.size) {
            val subList = ema1m.subList(startIndex, ema1m.size)
            val sign = (ema1m[startIndex - 1] - ema_base[startIndex - 1]) > 0 // sign is true if above, false if below
            result.add(Epoch(subList, sign))
        } else if (!crossingFound) {
            // If no crossings were found, return the whole list
            val sign = ema1m.first() > ema_base.first() // sign is true if above, false if below
            result.add(Epoch(ema1m, sign))
        }

        return result
    }

}