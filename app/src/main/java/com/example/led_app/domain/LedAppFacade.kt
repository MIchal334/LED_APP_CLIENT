package com.example.led_app.domain

import com.example.led_app.ports.inbound.LedClient
import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject

class LedAppFacade @Inject constructor(private val ledRepository: LedAppRepository, private val ledClient: LedClient) {
    fun getAllServersNameAndAddress(): List<Pair<String, String>> {
        return ledRepository.getAllKnownServerNameAddress()
    }

    suspend fun turnOffLed(ledIpAddress: String): Boolean {
        return ledClient.turnOffLed(ledIpAddress)
    }

    suspend fun testConnectionWithServer(ledIpAddress: String): Boolean {
        return ledClient.getTestConnection(ledIpAddress)
    }

    suspend fun saveNewLed(ledName: String, ledIpAddress: String): Boolean {
        val result = ledClient.getServerConfiguration(ledName, ledIpAddress)
        return result.fold(
            { false },
            { data -> ledRepository.saveNewLed(data) }
        )
    }

}