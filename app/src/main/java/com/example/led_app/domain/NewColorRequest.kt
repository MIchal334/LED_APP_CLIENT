package com.example.led_app.domain

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

data class NewColorRequest(
    override var ledIp: String,
    override var ledName: String,
    var redValue: Int,
    var blueValue: Int,
    var greenValue: Int,
    var changeServerId: Int
) : ServerRequest() {

    class BuilderType : NavType<Builder>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Builder? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): Builder {
            return Gson().fromJson(value, Builder::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: Builder) {
            bundle.putParcelable(key, value)
        }
    }

    @Parcelize
    class Builder(
        private var ledIp: String,
        private var ledName: String,
        private var redValue: Int,
        private var blueValue: Int,
        private var greenValue: Int,
        private var changeModeServerId: Int
    ) : Parcelable {

        fun getLedName(): String {
            return ledName
        }

        fun withLedName(ledName: String) = apply { this.ledName = ledName }
        fun withLedIp(ledIp: String) = apply { this.ledIp = Uri.encode(ledIp) }
        fun withRedValue(redValue: Int) = apply { this.redValue = redValue }
        fun withBlueValue(blueValue: Int) = apply { this.blueValue = blueValue }
        fun withGreenValue(greenValue: Int) = apply { this.greenValue = greenValue }
        fun withChangeModeServerId(changeServerId: Int) = apply { this.changeModeServerId = changeServerId }

        fun build() = NewColorRequest(ledIp, ledName, redValue, blueValue, greenValue, changeModeServerId)
    }

    override fun sendRequest(): Boolean {
        TODO("Not yet implemented")
    }
}


