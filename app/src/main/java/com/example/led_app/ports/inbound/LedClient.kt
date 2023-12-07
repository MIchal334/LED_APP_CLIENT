package com.example.led_app.ports.inbound

interface LedClient {

    fun checkConnectionWithServer(ipAddress: String): Boolean


}