package com.example.led_app.application.module

import com.example.led_app.addapters.outbound.LedAppRepositoryInMemory
import com.example.led_app.ports.outbound.LedAppRepository
import dagger.Module
import dagger.Provides

@Module
class FacadeModule {

    @Provides
    fun provideLedRepository(): LedAppRepository {
        return LedAppRepositoryInMemory()
    }
}