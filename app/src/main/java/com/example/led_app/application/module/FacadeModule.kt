package com.example.led_app.application.module

import LedClientSimulation
import com.example.led_app.addapters.outbound.LedAppRepositoryInMemory
import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.outbound.LedAppRepository
import dagger.Module
import dagger.Provides

@Module
class FacadeModule {

    @Provides
    fun provideLedRepository(): LedAppRepository {
        return LedAppRepositoryInMemory()
    }

    @Provides
    fun provideLedClient(): LedClient {
        return LedClientSimulation()
    }
}