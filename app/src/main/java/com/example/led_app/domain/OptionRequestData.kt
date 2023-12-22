package com.example.led_app.domain

data class OptionRequestData(
    val optionName: String,
    val endpoint: String,
    val requestMethod: HttpMethod? = HttpMethod.GET,
    val setColor: Boolean? = false,
//    val additionalParameterList: List<Pair<String, String>>? = listOf()
)

