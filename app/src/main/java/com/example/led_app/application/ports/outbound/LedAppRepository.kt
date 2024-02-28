package com.example.led_app.application.ports.outbound

import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedData
import com.example.led_app.domain.LedModeData


interface LedAppRepository {
    fun getAllKnownServerNameAddress(): List<Pair<String, String>>

    fun saveNewLed(ledToSave: LedData): Boolean

    fun getChangeModeByLedName(ledName: String): List<ChangeModeData>

    fun getModeByLedName(ledName: String): List<LedModeData>
    fun deleteLed(ledName: String): Boolean

    fun updateLed(ledData: LedData): Boolean
}