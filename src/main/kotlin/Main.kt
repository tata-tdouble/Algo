package org.example

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.bot.BotManager

fun main(): Unit = runBlocking {
        println("Hello from Algo!")
        launch {
            BotManager().startBot()
        }
}
