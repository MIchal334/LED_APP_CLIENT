package com.example.led_app.domain

import com.example.led_app.ports.inbound.LedClient
import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject

class LedAppFacade @Inject constructor(private val ledRepository: LedAppRepository, private val ledClient: LedClient) {
    fun getAllServersName(): List<String> {
        return ledRepository.getAllKnownServerName()
    }

    fun saveNewLed(ledName: String, ledIpAddress: String): Boolean {
        val result = ledClient.getServerConfiguration(ledName, ledIpAddress)
        result.fold(
            { return false },
            { data -> return ledRepository.saveNewLed(data) }
        )
    }

}