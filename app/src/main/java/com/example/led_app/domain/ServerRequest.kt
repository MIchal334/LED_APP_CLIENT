package com.example.led_app.domain

abstract class ServerRequest {
    abstract var ledIp: String
    abstract var ledName: String
    abstract fun sendRequest(): Boolean
}