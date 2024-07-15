package org.example.market.research

import org.example.market.MarketState
import org.koin.java.KoinJavaComponent.inject

class DataProcessor {

    private val marketState by inject<MarketState>(MarketState::class.java)

    val epochMap : MutableMap<EpochLevel, List<Epoch>> = mutableMapOf()

    fun getNrmalizer(): Normalizer {
        return Normalizer(genEpochData())
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
        val minSize = minOf(ema1m.size, ema_base.size)
        val trimmedEma1m = ema1m.take(minSize)
        val trimmedEmaBase = ema_base.take(minSize)

        val result = mutableListOf<Epoch>()
        if (trimmedEma1m.isEmpty() || trimmedEmaBase.isEmpty()) {
            throw IllegalArgumentException("The EMA lists must be non-empty.")
        }

        var startIndex = 0
        var crossingFound = false

        for (i in 1 until minSize) {
            val prevDiff = trimmedEma1m[i - 1] - trimmedEmaBase[i - 1]
            val currentDiff = trimmedEma1m[i] - trimmedEmaBase[i]

            if ((prevDiff <= 0 && currentDiff > 0) || (prevDiff >= 0 && currentDiff < 0)) {
                // Found a crossing
                if (i > startIndex) {
                    val subList = trimmedEma1m.subList(startIndex, i)
                    val sign = prevDiff > 0 // sign is true if above, false if below
                    val epoch = Epoch(subList, sign)
                    epoch.data()
                    result.add(epoch)
                }
                startIndex = i
                crossingFound = true
            }
        }

        // Add the remaining part if there was at least one crossing
        if (crossingFound && startIndex < minSize) {
            val subList = trimmedEma1m.subList(startIndex, minSize)
            val sign = (trimmedEma1m[startIndex - 1] - trimmedEmaBase[startIndex - 1]) > 0 // sign is true if above, false if below
            result.add(Epoch(subList, sign))
        } else if (!crossingFound) {
            // If no crossings were found, return the whole list
            val sign = trimmedEma1m.first() > trimmedEmaBase.first() // sign is true if above, false if below
            result.add(Epoch(trimmedEma1m, sign))
        }

        return result
    }

}