package com.example.led_app.ports.inbound

import arrow.core.Either
import com.example.led_app.domain.LedData
import com.example.led_app.domain.NewColorRequest

interface LedClient {
    suspend fun getTestConnection(ipAddress: String): Boolean
    suspend fun getServerConfiguration(
        ledName: String,
        ipAddress: String,
    ): Either<RuntimeException, LedData>

    suspend fun turnOffLed(ipAddress: String): Boolean

    suspend fun sendColorRequest(colorRequest: NewColorRequest): Boolean

}