package org.example.di

import org.example.auth.AuthManager
import org.example.market.Controller
import org.example.market.MarketState
import org.example.market.Network
import org.example.market.research.DataProcessor
import org.example.strategy.Functions
import org.example.strategy.Strategy
import org.example.strategy.StrategyState
import org.example.trade.BTC_USDT_Trading_Bot
import org.example.trade.TradeState
import org.example.user.UserOptions
import org.example.user.UserState
import org.example.util.AlgoLogger
import org.example.util.FileResource
import org.koin.dsl.module

val applicationModules = module{
    single { UserState() }
    single { UserOptions() }
    single { AuthManager() }
    single { Controller() }
    single { Network() }
    single { AlgoLogger() }
    single { FileResource() }
    single { MarketState() }
    single { StrategyState() }
    single { Functions() }
    single { Strategy() }
    single { TradeState() }
    single { BTC_USDT_Trading_Bot() }
    single { DataProcessor() }
}