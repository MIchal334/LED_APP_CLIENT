package com.example.led_app.application

import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.outbound.LedAppRepository
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedModeData
import com.example.led_app.domain.NewServerRequest
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


    suspend fun updateLedConfig(ledName: String, ledIpAddress: String): Boolean {
        val result = ledClient.getServerConfiguration(ledName, ledIpAddress)
        return result.fold(
            { false },
            { data ->
                ledRepository.updateLed(data) }
        )
    }
    fun getChangesModeByName(ledName: String): List<ChangeModeData> {
       return ledRepository.getChangeModeByLedName(ledName);
    }

    fun getLedModeByName(ledName: String): List<LedModeData> {
        return ledRepository.getModeByLedName(ledName);
    }


    suspend fun sendColorRequest(colorRequest: NewServerRequest): Boolean {
        return ledClient.sendColorRequest(colorRequest)
    }

    suspend fun sendModeRequest(colorRequest: NewServerRequest): Boolean {
        return ledClient.sendModeRequest(colorRequest)
    }
}