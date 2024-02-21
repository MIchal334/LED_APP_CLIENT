package com.example.led_app.application.ports.outbound

import com.example.led_app.domain.LedTest


interface LedAppRepositoryTest {

    fun saveNewLed(ledToSave: LedTest): Long
}