package org.example.market.research

enum class EpochLevel{ EMA5, EMA23, EMA80, EMA200 }

data class Epoch(
    val data: List<Double>,
    val sign: Boolean
) {
    val high: Double = if (sign) { data.max() } else { data.last() }
    val low: Double = if (sign) { data.last() } else { data.min() }

    val entry_price: Double = data.firstOrNull() ?: Double.NaN
    val exit_price: Double = data.lastOrNull() ?: Double.NaN

    val good_market_time: Int = if (sign) {
        data.indexOf(high)
    } else {
        data.lastIndex - data.indexOf(low)
    }

    val bad_market_time: Int = if (sign) {
        data.lastIndex - data.indexOf(high)
    } else {
        data.indexOf(low)
    }

    val inc_floctuation_percent: Double = if (sign) {
        ( (high - data.first()) / data.first() ) * 100
    } else {
        ( ( data.last() - high) / low ) * 100
    }

    val dec_floctuation_percent: Double = if (sign) {
        ( (low) / data.first()  ) * 100
    } else {
        ( ( data.last() - low ) / low ) * 100
    }

    val epochTime = data.size

    fun data(){
        println("high $high")
        println("low $low")
        println("entry_price $entry_price")
        println("exit_price $exit_price")
        println("good_market_time $good_market_time")
        println("bad_market_time $exit_price")
        println("inc_floctuation_percent $inc_floctuation_percent")
        println("dec_floctuation_percent $dec_floctuation_percent")
    }

}

