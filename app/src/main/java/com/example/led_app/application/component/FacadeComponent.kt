package com.example.led_app.application.component

import com.example.led_app.application.module.FacadeModule
import com.example.led_app.application.LedAppFacade
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FacadeModule::class])
interface FacadeComponent {
    fun injectFacade(): LedAppFacade
}