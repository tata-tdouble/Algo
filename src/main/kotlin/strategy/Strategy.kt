package org.example.strategy

import org.example.market.MarketState
import org.example.util.AlgoLogger
import org.koin.java.KoinJavaComponent.inject

class Strategy {

    private val marketState by inject<MarketState>(MarketState::class.java)
    private val functions by inject<Functions>(Functions::class.java)

    fun entry_calculate_uptrend(): Boolean {
        return (functions.c_first_two(marketState._ema_23.value) > 0)
                && (functions.c_first_two(marketState._ema_80.value) > 0) &&
                (functions.c_first_two(marketState._ema_200.value) > 0)
    }

    fun entry_calculate_overall_positive_gradient(): Boolean {
        return entry_calculate_last_positive_gradient() && entry_calculate_first_positive_gradient()
    }

    fun entry_calculate_first_positive_gradient(): Boolean {
        if (functions.c_first_two(marketState._ema_1.value) > functions.c_first_two(marketState._ema_5.value)) {
            if (functions.c_first_two(marketState._ema_5.value) > functions.c_first_two(marketState._ema_23.value)) {
                if (functions.c_first_two(marketState._ema_23.value) > functions.c_first_two(marketState._ema_80.value)) {
                    if (functions.c_first_two(marketState._ema_80.value) > functions.c_first_two(marketState._ema_200.value)) {
                        return true
                    } else {
                        return false
                    }
                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    fun entry_calculate_last_positive_gradient(): Boolean {
        if (functions.c_last_two(marketState._ema_1.value) > functions.c_last_two(marketState._ema_5.value)) {
            if (functions.c_last_two(marketState._ema_5.value) > functions.c_last_two(marketState._ema_23.value)) {
                if (functions.c_last_two(marketState._ema_23.value) > functions.c_last_two(marketState._ema_80.value)) {
                    if (functions.c_last_two(marketState._ema_80.value) > functions.c_last_two(marketState._ema_200.value)) {
                        return true
                    } else {
                        return false
                    }
                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    fun entry_calculate_overall_weight(value : Double = 100.0): Boolean {
        val v1 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value))
        val v2 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_5.value))
        val v3 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_23.value))
        val v4 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_80.value))
        val v5 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value))
        return (v1 + v2 + v3 + v4 + v5) > value
    }

    fun entry_disparity_of_ema(): Boolean {
        val arrayList = ArrayList<Double>()
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_5.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_23.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_80.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value)))
        val diff = arrayList.max() - arrayList.min()
        val g1 = functions.c_first_two(marketState._ema_1.value)
        val g2 = functions.c_first_two(marketState._ema_5.value)
        return diff > 90 && g1 > 0.0 && g2 > 0.0
    }

    fun entry_stock_rsi_1(): Boolean {
        val v1 = marketState._vk_rsi.value.first()
        val v2 = marketState._vd_rsi.value.first()
        return v1 > v2
    }

    fun entry_stock_rsi_2(): Boolean {
        val v1 = functions.c_first_two(marketState._vk_rsi.value)
        val v2 = functions.c_first_two(marketState._vd_rsi.value)
        return v1 > 75 || v2 > 75
    }

    fun entry_calculate_high_point_angle(): Boolean {
        val v1 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value))
        val v5 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value))
        val ev1 = v1 > 45
        val ev2 = v5 > 0
        return  ev1 && ev2 && entry_calculate_overall_weight(35.0)
    }

    fun entry_calculate_line_cut_from_under(): Boolean {
        val ev1 = marketState._ema_1.value.first() > marketState._ema_200.value.first()
        val ev2 = marketState._ema_1.value.last() < marketState._ema_200.value.last()
        return  ev1 && ev2 && entry_calculate_overall_weight(35.0)
    }


    fun exit_calculate_overall_weight(value : Double = 9.0): Boolean {
        val v1 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value))
        val v2 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_5.value))
        val v3 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_23.value))
        val v4 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_80.value))
        val v5 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value))
        return (v1 + v2 + v3 + v4 + v5) < value
    }

    fun exit_disparity_of_ema(): Boolean {
        val arrayList = ArrayList<Double>()
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_5.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_23.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_80.value)))
        arrayList.add(functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value)))
        val diff = arrayList.max() - arrayList.min()
        val g1 = functions.c_first_two(marketState._ema_1.value)
        val g2 = functions.c_first_two(marketState._ema_5.value)
        return diff > 60 && g1 < 0 && g2 < 0
    }

    fun exit_stock_rsi_1(): Boolean {
        val v1 = marketState._vk_rsi.value.first()
        val v2 = marketState._vd_rsi.value.first()
        return v1 < v2
    }

    fun exit_stock_rsi_2(): Boolean {
        val v1 = marketState._vk_rsi.value.first()
        val v2 = marketState._vd_rsi.value.first()
        return v1 < 70 && v2 < 70
    }

    fun exit_stop_loss(): Boolean {
        return functions.c_first_two(marketState._ema_1.value) < 0.0
    }

    fun exit_calculate_high_point_angle(): Boolean {
        val v1 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_1.value))
        val v5 = functions.c_tan_inverse(functions.c_first_two(marketState._ema_200.value))
        val ev1 = v1 < 30
        val ev2 = v5 > 4
        return  ev1 && ev2 && entry_calculate_overall_weight()
    }

    fun exit_calculate_high_point_value(): Boolean {
        val ev1 =  marketState._ema_1.value.first() < marketState._ema_5.value.first()
        val ev2 =  marketState._ema_1.value.first() < marketState._ema_23.value.first()
        return ev1 || ev2
    }

}