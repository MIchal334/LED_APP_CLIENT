package com.example.led_app.domain

data class LedModeData(
    val optionName: String,
    val internalServerNumber: Int,
    val setColor: Boolean? = false
)

