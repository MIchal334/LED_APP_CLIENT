package com.example.led_app.ports.outbound


interface LedAppRepository {
    fun getAllKnownServerName(): List<String>
}