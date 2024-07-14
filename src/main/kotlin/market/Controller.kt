package org.example.market


import kotlinx.coroutines.time.delay
import org.example.market.research.DataProcessor
import org.example.strategy.StrategyState
import org.example.trade.BTC_USDT_Trading_Bot
import org.koin.java.KoinJavaComponent.inject
import java.time.Duration


class Controller {

    private val network by inject<Network>(Network::class.java)
    private val strategyState by inject<StrategyState>(StrategyState::class.java)
    private val binanceBot by inject<BTC_USDT_Trading_Bot>(BTC_USDT_Trading_Bot::class.java)
    private val dataProcessor by inject<DataProcessor>(DataProcessor::class.java)


    suspend fun takeControl() {

        while (true) {

//            logger.log_info("")
//            logger.log_info("")
//            logger.log_info("##########################")
//            logger.log_info("##  Trade Cycle Start  ###")
//            logger.log_info("##########################")
//            logger.log_info("")
//            logger.log_info("")

            val loopStartTime = System.currentTimeMillis()

            // Load EMA Data
            var timeBefore = System.currentTimeMillis()
            network.loadEMAData()
            var timeAfter = System.currentTimeMillis()
            var timeDiff = timeAfter - timeBefore
//            logger.log_warn("EMA Data Load Time: $timeDiff ms")

            // Delay 15 seconds
            val delayTime = 15000L
            delay(Duration.ofMillis(delayTime))

            // Load RSI Data
            timeBefore = System.currentTimeMillis()
            network.loadRsiData()
            timeAfter = System.currentTimeMillis()
            timeDiff = timeAfter - timeBefore
//            logger.log_warn("RSI Data Load Time: $timeDiff ms")

            // Update strategy state and execute bot
            timeBefore = System.currentTimeMillis()
            val prediction = dataProcessor.predict()
            strategyState.update()
            prediction.printPrediction()
            //val signal = strategyState.generate_signal(prediction)
            //strategyState.printStrategyState()
            //binanceBot.entry_listener_exec()
            //binanceBot.exit_listener_exec()
            binanceBot.printResult()
            timeAfter = System.currentTimeMillis()
            timeDiff = timeAfter - timeBefore
//            logger.log_warn("Strategy State Update Time: $timeDiff ms")

            // Calculate total loop time
            val loopEndTime = System.currentTimeMillis()
            val totalLoopTime = loopEndTime - loopStartTime
//            logger.log_warn("Total Loop Time: $totalLoopTime ms")

            // Calculate remaining time to achieve exactly 300100 ms per loop
            val remainingTime = 60100 - totalLoopTime
//            logger.log_warn("Remaining Time: $remainingTime ms")

            // Delay the remaining time
            delay(Duration.ofMillis(maxOf(0, remainingTime)))
        }
    }

}