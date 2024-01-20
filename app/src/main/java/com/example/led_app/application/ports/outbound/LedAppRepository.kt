package com.example.led_app.application.ports.outbound

import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedData


interface LedAppRepository {
    fun getAllKnownServerNameAddress(): List<Pair<String,String>>

    fun saveNewLed(ledToSave: LedData): Boolean

    fun getChangeModeByLedName(ledName:String): List<ChangeModeData>
}