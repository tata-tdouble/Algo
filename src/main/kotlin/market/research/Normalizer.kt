package org.example.market.research

import org.example.util.calculateMode
import org.example.util.calculateSecondMode

data class Normalizer(val epochMap : MutableMap<EpochLevel, List<Epoch>>) {

    // StateFlow holds default]
    val average_bullish_time_ema5 = epochMap.get(EpochLevel.EMA5)!!.map { it.good_market_time }.average()
    val average_bearish_time_ema5 = epochMap.get(EpochLevel.EMA5)!!.map { it.bad_market_time }.average()

    val average_bullish_time_ema23 = epochMap.get(EpochLevel.EMA23)!!.map { it.good_market_time }.average()
    val average_bearish_time_ema23 = epochMap.get(EpochLevel.EMA23)!!.map { it.bad_market_time }.average()

    val average_bullish_time_ema80 = epochMap.get(EpochLevel.EMA80)!!.map { it.good_market_time }.average()
    val average_bearish_time_ema80 = epochMap.get(EpochLevel.EMA80)!!.map { it.bad_market_time }.average()

    val average_bullish_time_ema200 = epochMap.get(EpochLevel.EMA80)!!.map { it.good_market_time }.average()
    val average_bearish_time_ema200 = epochMap.get(EpochLevel.EMA80)!!.map { it.bad_market_time }.average()

    val median_bullish_time_ema5_1 = epochMap.get(EpochLevel.EMA5)!!.map { it.good_market_time }.calculateMode()
    val median_bullish_time_ema5_2 = epochMap.get(EpochLevel.EMA5)!!.map { it.good_market_time }.calculateSecondMode()
    val median_bearish_time_ema5_1 = epochMap.get(EpochLevel.EMA5)!!.map { it.bad_market_time }.calculateMode()
    val median_bearish_time_ema5_2 = epochMap.get(EpochLevel.EMA5)!!.map { it.bad_market_time }.calculateSecondMode()

    val median_bullish_time_ema23_1 = epochMap.get(EpochLevel.EMA23)!!.map { it.good_market_time }.calculateMode()
    val median_bullish_time_ema23_2 = epochMap.get(EpochLevel.EMA23)!!.map { it.good_market_time }.calculateSecondMode()
    val median_bearish_time_ema23_1 = epochMap.get(EpochLevel.EMA23)!!.map { it.bad_market_time }.calculateMode()
    val median_bearish_time_ema23_2 = epochMap.get(EpochLevel.EMA23)!!.map { it.bad_market_time }.calculateSecondMode()

    val median_bullish_time_ema80_1 = epochMap.get(EpochLevel.EMA80)!!.map { it.good_market_time }.calculateMode()
    val median_bullish_time_ema80_2 = epochMap.get(EpochLevel.EMA80)!!.map { it.good_market_time }.calculateSecondMode()
    val median_bearish_time_ema80_1 = epochMap.get(EpochLevel.EMA80)!!.map { it.bad_market_time }.calculateMode()
    val median_bearish_time_ema80_2 = epochMap.get(EpochLevel.EMA80)!!.map { it.bad_market_time }.calculateSecondMode()

    val median_bullish_time_ema200_1 = epochMap.get(EpochLevel.EMA200)!!.map { it.good_market_time }.calculateMode()
    val median_bullish_time_ema200_2 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.good_market_time }.calculateSecondMode()
    val median_bearish_time_ema200_1 = epochMap.get(EpochLevel.EMA200)!!.map { it.bad_market_time }.calculateMode()
    val median_bearish_time_ema200_2 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.bad_market_time }.calculateSecondMode()


    val average_bullish_price_percentage_ema5 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.inc_floctuation_percent }.average()
    val average_bearish_price_percentage_ema5 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.dec_floctuation_percent }.average()

    val average_bullish_price_percentage_ema23 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.inc_floctuation_percent }.average()
    val average_bearish_price_percentage_ema23 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.dec_floctuation_percent }.average()

    val average_bullish_price_percentage_ema80 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.inc_floctuation_percent }.average()
    val average_bearish_price_percentage_ema80 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.dec_floctuation_percent }.average()

    val average_bullish_price_percentage_ema200 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.inc_floctuation_percent }.average()
    val average_bearish_price_percentage_ema200 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.inc_floctuation_percent }.average()

    val median_bullish_price_percentage_ema5_1 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.inc_floctuation_percent }.calculateMode()
    val median_bullish_price_percentage_ema5_2 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.inc_floctuation_percent }.calculateSecondMode()
    val median_bearish_price_percentage_ema5_1 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.dec_floctuation_percent }.calculateMode()
    val median_bearish_price_percentage_ema5_2 =
        epochMap.get(EpochLevel.EMA5)!!.map { it.dec_floctuation_percent }.calculateSecondMode()

    val median_bullish_price_percentage_ema23_1 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.inc_floctuation_percent }.calculateMode()
    val median_bullish_price_percentage_ema23_2 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.inc_floctuation_percent }.calculateSecondMode()
    val median_bearish_price_percentage_ema23_1 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.dec_floctuation_percent }.calculateMode()
    val median_bearish_price_percentage_ema23_2 =
        epochMap.get(EpochLevel.EMA23)!!.map { it.dec_floctuation_percent }.calculateSecondMode()

    val median_bullish_price_percentage_ema80_1 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.inc_floctuation_percent }.calculateMode()
    val median_bullish_price_percentage_ema80_2 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.inc_floctuation_percent }.calculateSecondMode()
    val median_bearish_price_percentage_ema80_1 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.dec_floctuation_percent }.calculateMode()
    val median_bearish_price_percentage_ema80_2 =
        epochMap.get(EpochLevel.EMA80)!!.map { it.dec_floctuation_percent }.calculateSecondMode()

    val median_bullish_price_percentage_ema200_1 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.inc_floctuation_percent }.calculateMode()
    val median_bullish_price_percentage_ema200_2 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.inc_floctuation_percent }.calculateSecondMode()
    val median_bearish_price_percentage_ema200_1 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.dec_floctuation_percent }.calculateMode()
    val median_bearish_price_percentage_ema200_2 =
        epochMap.get(EpochLevel.EMA200)!!.map { it.dec_floctuation_percent }.calculateSecondMode()


    fun printValues() {
        println()
        println("**************************************************")
        println()

        println("average_bullish_time_ema5 : $average_bullish_time_ema5")
        println("average_bearish_time_ema5 : $average_bearish_time_ema5")
        println("average_bullish_time_ema23 : $average_bullish_time_ema23")
        println("average_bearish_time_ema23 : $average_bearish_time_ema23")
        println("average_bullish_time_ema80 : $average_bullish_time_ema80")
        println("average_bearish_time_ema80 : $average_bearish_time_ema80")
        println("average_bullish_time_ema200 : $average_bullish_time_ema200")
        println("average_bearish_time_ema200 : $average_bearish_time_ema200")
        println()
        println("median_bullish_time_ema5_1 : $median_bullish_time_ema5_1")
        println("median_bullish_time_ema5_2 : $median_bullish_time_ema5_2")
        println("median_bearish_time_ema5_1 : $median_bearish_time_ema5_1")
        println("median_bearish_time_ema5_2 : $median_bearish_time_ema5_2")
        println("median_bullish_time_ema23_1 : $median_bullish_time_ema23_1")
        println("median_bullish_time_ema23_2 : $median_bullish_time_ema23_2")
        println("median_bearish_time_ema23_1 : $median_bearish_time_ema23_1")
        println("median_bearish_time_ema23_2 : $median_bearish_time_ema23_2")
        println("median_bullish_time_ema80_1 : $median_bullish_time_ema80_1")
        println("median_bullish_time_ema80_2 : $median_bullish_time_ema80_2")
        println("median_bearish_time_ema80_1 : $median_bearish_time_ema80_1")
        println("median_bearish_time_ema80_2 : $median_bearish_time_ema80_2")
        println("median_bullish_time_ema200_1 : $median_bullish_time_ema200_1")
        println("median_bullish_time_ema200_2 : $median_bullish_time_ema200_2")
        println("median_bearish_time_ema200_1 : $median_bearish_time_ema200_1")
        println("median_bearish_time_ema200_2 : $median_bearish_time_ema200_2")
        println()
        println("average_bullish_price_percentage_ema5 : $average_bullish_price_percentage_ema5")
        println("average_bearish_price_percentage_ema5 : $average_bearish_price_percentage_ema5")
        println("average_bullish_price_percentage_ema23 : $average_bullish_price_percentage_ema23")
        println("average_bearish_price_percentage_ema23 : $average_bearish_price_percentage_ema23")
        println("average_bullish_price_percentage_ema80 : $average_bullish_price_percentage_ema80")
        println("average_bearish_price_percentage_ema80 : $average_bearish_price_percentage_ema80")
        println("average_bullish_price_percentage_ema200 : $average_bullish_price_percentage_ema200")
        println("average_bearish_price_percentage_ema200 : $average_bearish_price_percentage_ema200")
        println()
        println("median_bullish_price_percentage_ema5_1 : $median_bullish_price_percentage_ema5_1")
        println("median_bullish_price_percentage_ema5_2 : $median_bullish_price_percentage_ema5_2")
        println("median_bearish_price_percentage_ema5_1 : $median_bearish_price_percentage_ema5_1")
        println("median_bearish_price_percentage_ema5_2 : $median_bearish_price_percentage_ema5_2")
        println("median_bullish_price_percentage_ema23_1 : $median_bullish_price_percentage_ema23_1")
        println("median_bullish_price_percentage_ema23_2 : $median_bullish_price_percentage_ema23_2")
        println("median_bearish_price_percentage_ema23_1 : $median_bearish_price_percentage_ema23_1")
        println("median_bearish_price_percentage_ema23_2 : $median_bearish_price_percentage_ema23_2")
        println("median_bullish_price_percentage_ema80_1 : $median_bullish_price_percentage_ema80_1")
        println("median_bullish_price_percentage_ema80_2 : $median_bullish_price_percentage_ema80_2")
        println("median_bearish_price_percentage_ema80_1 : $median_bearish_price_percentage_ema80_1")
        println("median_bearish_price_percentage_ema80_2 : $median_bearish_price_percentage_ema80_2")
        println("median_bullish_price_percentage_ema200_1 : $median_bullish_price_percentage_ema200_1")
        println("median_bullish_price_percentage_ema200_2 : $median_bullish_price_percentage_ema200_2")
        println("median_bearish_price_percentage_ema200_1 : $median_bearish_price_percentage_ema200_1")
        println("median_bearish_price_percentage_ema200_2 : $median_bearish_price_percentage_ema200_2")

    }

    fun get_Bullish_Normalized_EMA5(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA5)!!
        val list1 = raw.filter { it.good_market_time > (0.7 * average_bullish_time_ema5) }
        val list2 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema5_1!!) }
        val list3 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema5_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { it.sign }.distinct()
    }

    fun get_Bearish_Normalized_EMA5(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA5)!!
        val list1 = raw.filter { it.bad_market_time > (0.7 * average_bearish_time_ema5) }
        val list2 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema5_1!!) }
        val list3 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema5_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { !it.sign }.distinct()
    }


    fun get_Bullish_Normalized_EMA23(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA23)!!
        val list1 = raw.filter { it.good_market_time > (0.7 * average_bullish_time_ema23) }
        val list2 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema23_1!!) }
        val list3 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema23_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { it.sign }.distinct()
    }

    fun get_Bearish_Normalized_EMA23(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA23)!!
        val list1 = raw.filter { it.bad_market_time > (0.7 * average_bearish_time_ema23) }
        val list2 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema23_1!!) }
        val list3 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema23_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { !it.sign }.distinct()
    }


    fun get_Bullish_Normalized_EMA80(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA80)!!
        val list1 = raw.filter { it.good_market_time > (0.7 * average_bullish_time_ema80) }
        val list2 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema80_1!!) }
        val list3 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema80_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { it.sign }.distinct()
    }

    fun get_Bearish_Normalized_EMA80(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA23)!!
        val list1 = raw.filter { it.bad_market_time > (0.7 * average_bearish_time_ema80) }
        val list2 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema80_1!!) }
        val list3 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema80_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { !it.sign }.distinct()
    }


    fun get_Bullish_Normalized_EMA200(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA200)!!
        val list1 = raw.filter { it.good_market_time > (0.7 * average_bullish_time_ema200) }
        val list2 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema200_1!!) }
        val list3 = raw.filter { it.good_market_time > (0.7 * median_bullish_time_ema200_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { it.sign }.distinct()
    }

    fun get_Bearish_Normalized_EMA200(): List<Epoch> {
        val raw = epochMap.get(EpochLevel.EMA200)!!
        val list1 = raw.filter { it.bad_market_time > (0.7 * average_bearish_time_ema200) }
        val list2 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema200_1!!) }
        val list3 = raw.filter { it.bad_market_time > (0.7 * median_bearish_time_ema200_2!!) }
        return buildList {
            addAll(list1)
            addAll(list2)
            addAll(list3)
        }.filter { !it.sign }.distinct()
    }

}

