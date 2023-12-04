package com.example.led_app.addapters.outbound

import com.example.led_app.ports.outbound.LedAppRepository
import javax.inject.Inject

class LedAppRepositoryInMemory @Inject constructor() : LedAppRepository {
    override fun getAllKnownServerName(): List<String> {
        return listOf("L1", "L2", "L3", "L1", "L2", "L3", "L1", "L2", "L3", "L1", "L2", "L3", "L1", "L2", "L3","L5", "L6", "L7")
    }
}