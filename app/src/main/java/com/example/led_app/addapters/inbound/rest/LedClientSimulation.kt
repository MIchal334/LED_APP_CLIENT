package com.example.led_app.addapters.inbound.rest

import com.example.led_app.ports.inbound.LedClient

class LedClientSimulation : LedClient {
    private val serverAddress: String = "http://localhost:8090/"
    override fun checkConnectionWithServer(ipAddress: String): Boolean {
        val response = khttp.get(serverAddress + ipAddress)
        if (response.statusCode == 200) {
            return true
        }
        return false
    }
}