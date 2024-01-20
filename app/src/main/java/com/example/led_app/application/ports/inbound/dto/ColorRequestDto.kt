package com.example.led_app.application.ports.inbound.dto

import com.example.led_app.domain.NewServerRequest

class ColorRequestDto(
    var redValue: Int,
    var blueValue: Int,
    var greenValue: Int,
    var changeServerId: Int,
) {
    companion object {
        fun of(colorRequest: NewServerRequest): ColorRequestDto {
            return ColorRequestDto(
                redValue = colorRequest.redValue,
                blueValue = colorRequest.blueValue,
                greenValue = colorRequest.greenValue,
                changeServerId = colorRequest.changeServerId
            )
        }
    }

}