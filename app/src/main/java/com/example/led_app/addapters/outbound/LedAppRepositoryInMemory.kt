package com.example.led_app.addapters.outbound

import android.content.res.Resources.NotFoundException
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedData
import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject
import kotlin.streams.toList

class LedAppRepositoryInMemory @Inject constructor() : LedAppRepository {

    val ledList: MutableList<LedData> = mutableListOf()
    override fun getAllKnownServerNameAddress(): List<Pair<String, String>> {
        return ledList.stream().map { led -> Pair(led.ledName, led.ipAddress) }.toList()
    }

    override fun saveNewLed(ledToSave: LedData): Boolean {
        this.ledList.add(ledToSave)
        return true;
    }

    override fun getChangeModeByLedName(ledName: String): List<ChangeModeData> {
        val foundLed = ledList.find { it.ledName == ledName }
            ?: throw NotFoundException("Nie znaleziono LED o nazwie: $ledName")

        return foundLed.changeModes
    }


}