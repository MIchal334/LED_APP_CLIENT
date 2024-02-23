package com.example.led_app.application.module

import AppDatabase
import LedClientSimulation
import android.content.Context
import androidx.room.Room
import com.example.led_app.BuildConfig
import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.outbound.LedAppRepository
import dagger.Module
import dagger.Provides

@Module
class FacadeModule(val applicationContext: Context) {
    val db: AppDatabase

    init {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun provideLedRepository(): LedAppRepository {
        return db.ledDao()
    }

    @Provides
    fun provideLedClient(): LedClient {
        if (BuildConfig.FLAVOR == "stub") {
//            return StubLedClient()
        }
        return LedClientSimulation()
    }


}