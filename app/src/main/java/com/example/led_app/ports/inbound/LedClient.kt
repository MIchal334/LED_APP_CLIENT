package com.example.led_app.ports.inbound

import arrow.core.Either
import com.example.led_app.domain.LedData

interface LedClient {
    fun getServerConfiguration(ledName: String, ipAddress: String): Either<RuntimeException, LedData>

}