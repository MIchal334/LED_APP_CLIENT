package com.example.led_app.domain

import com.example.led_app.ports.inbound.LedClient
import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject

class LedAppFacade @Inject constructor(private val ledRepository: LedAppRepository, private val ledClient: LedClient) {
    fun getAllServersName(): List<String> {
        return ledRepository.getAllKnownServerName()
    }

    fun saveNewLed(ledName: String, ledIpAddress: String) {
        ledClient.checkConnectionWithServer(ledIpAddress)
        val ledToSave = LedData(ledName = ledName, ipAddress = ledIpAddress)
        ledRepository.saveNewLed(ledToSave)
    }

}