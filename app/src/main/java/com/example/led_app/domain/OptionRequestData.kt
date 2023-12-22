package com.example.led_app.domain

data class OptionRequestData(
    val optionName: String,
    val internalServerNumber: Int,
    val setColor: Boolean? = false
)

