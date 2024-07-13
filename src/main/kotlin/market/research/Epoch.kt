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
        data.indexOf(high) - data.indexOf(low)
    }

    val bad_market_time: Int = if (sign) {
        data.indexOf(low) - data.indexOf(high)
    } else {
        data.indexOf(low)
    }

    val inc_floctuation_percent: Double = if (sign) {
        ( (data.first() - high) / data.first() ) * 100
    } else {
        ( ( high - low) / low ) * 100
    }

    val dec_floctuation_percent: Double = if (sign) {
        ( (low - high) / low ) * 100
    } else {
        ( ( low - data.first() ) / low ) * 100
    }

}

