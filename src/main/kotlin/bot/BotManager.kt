package org.example.bot

interface BotManager {
    suspend fun startBot()
    fun stopBot()
}