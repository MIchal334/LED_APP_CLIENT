package com.example.led_app.application.module

import LedClientSimulation
import android.content.Context
import com.example.led_app.BuildConfig
import com.example.led_app.addapters.outbound.LedAppRepositoryInMemory
import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.outbound.LedAppRepository
import dagger.Module
import dagger.Provides

@Module
class FacadeModule(val applicationContext: Context) {
//    val db: AppDatabase
//
//    init {
//        db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "database-name"
//        ).build()
//    }

    @Provides
    fun provideLedRepository(): LedAppRepository {
        return LedAppRepositoryInMemory()
    }

    @Provides
    fun provideLedClient(): LedClient {
        if (BuildConfig.FLAVOR == "stub") {
//            return StubLedClient()
        }
        return LedClientSimulation()
    }


}