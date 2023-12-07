package com.example.led_app.domain

data class LedData(
    val ledName: String = "A",
    val ipAddress: String = "127.0.0.1",
    val ledModes: List<OptionRequestData> = listOf(OptionRequestData("rain","/rain")),
    val changeModes: List<OptionRequestData> = listOf(OptionRequestData("snake","/snake"))
)


