package com.example.led_app.domain

import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject

class LedAppFacade @Inject constructor(private val ledRepository: LedAppRepository) {
    fun getAllServersName(): List<String> {
        return ledRepository.getAllKnownServerName()
    }
}