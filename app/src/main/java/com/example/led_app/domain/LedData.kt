package com.example.led_app.domain

data class LedData(
    val ledName: String = "A",
    val ipAddress: String = "127.0.0.1",
    val ledModes: List<OptionAndEndpointPair> = listOf(OptionAndEndpointPair("rain","/rain")),
    val changeModes: List<OptionAndEndpointPair> = listOf(OptionAndEndpointPair("snake","/snake"))
)


