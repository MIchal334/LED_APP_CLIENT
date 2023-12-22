package com.example.led_app.ports.outbound

import com.example.led_app.domain.LedData


interface LedAppRepository {
    fun getAllKnownServerNameAddress(): List<Pair<String,String>>

    fun saveNewLed(ledToSave: LedData): Boolean
}