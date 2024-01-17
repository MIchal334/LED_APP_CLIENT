package com.example.led_app.domain

data class LedModeData(
    val optionName: String,
    val modeServerId: Int,
    val setColor: Boolean? = false
)

