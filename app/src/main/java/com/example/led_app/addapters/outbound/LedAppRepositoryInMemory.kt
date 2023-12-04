package com.example.led_app.addapters.outbound

import com.example.led_app.domain.LedData
import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject
import kotlin.streams.toList

class LedAppRepositoryInMemory @Inject constructor() : LedAppRepository {

    val ledList: MutableList<LedData> = mutableListOf()
    override fun getAllKnownServerName(): List<String> {
        return ledList.stream().map { led -> led.ledName }.toList()
    }

    override fun saveNewLed(ledToSave: LedData): Boolean {
        this.ledList.add(ledToSave)
        return true;
    }
}