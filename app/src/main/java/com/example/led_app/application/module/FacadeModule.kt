package com.example.led_app.application.module

import LedClientSimulation
import android.content.Context
import androidx.room.Room
import com.example.led_app.BuildConfig
import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.outbound.LedAppRepository
import com.example.led_app.config.AppDatabase
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides

@Module
class FacadeModule() {

    @BindsInstance
    abstract fun application(app: YourApp): Context
    @Provides
    fun provideLedRepository(): LedAppRepository {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    fun provideLedClient(): LedClient {
        if (BuildConfig.FLAVOR == "stub") {
//            return StubLedClient()
        }
        return LedClientSimulation()
    }
}