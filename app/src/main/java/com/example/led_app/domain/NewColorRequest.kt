package com.example.led_app.domain

import android.net.Uri

class NewColorRequest(
    override var ledIp: String, override var ledName: String,
    var redValue: Int,
    var blueValue: Int,
    var greenValue: Int,
    var changeServerId: Int
) : ServerRequest() {

    class Builder() {
        private var ledIp: String = ""
        private var ledName: String = ""
        private var redValue: Int = 0
        private var blueValue: Int = 0
        private var greenValue: Int = 0
        private var changeServerId: Int = 0

        fun withLedName(ledName: String) = apply { this.ledName = ledName }
        fun withLedIp(ledIp: String) = apply { this.ledIp = Uri.encode(ledIp) }
        fun withRedValue(redValue: Int) = apply { this.redValue = redValue }
        fun withBlueValue(blueValue: Int) = apply { this.blueValue = blueValue }
        fun withGreenValue(greenValue: Int) = apply { this.greenValue = greenValue }
        fun withChangeServerId(changeServerId: Int) = apply { this.changeServerId = changeServerId }

        fun build() = NewColorRequest(ledIp, ledName, redValue, blueValue, greenValue, changeServerId)
    }

    override fun sendRequest(): Boolean {
        TODO("Not yet implemented")
    }
}