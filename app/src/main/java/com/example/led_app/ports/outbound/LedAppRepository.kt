package com.example.led_app.ports.outbound

import com.example.led_app.domain.LedData


interface LedAppRepository {
    fun getAllKnownServerName(): List<String>

    fun saveNewLed(ledToSave: LedData)
}