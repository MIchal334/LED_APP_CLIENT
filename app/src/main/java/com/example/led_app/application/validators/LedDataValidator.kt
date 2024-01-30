package com.example.led_app.application.validators

import com.example.led_app.domain.ConstantsString.LED_DATA_NOT_VALID
import java.net.InetAddress

class LedDataValidator {
    companion object {
        fun valid(ledName: String, ledIp: String, ledNameList: List<String>): Pair<Boolean, String> {
            return Pair(validName(ledName, ledNameList) && validIp(ledIp), LED_DATA_NOT_VALID)
        }


        private fun validName(ledName: String, ledNameList: List<String>): Boolean {
            return ledNameList.find { it == ledName } == null
        }

        private fun validIp(ledIp: String): Boolean {
            val ipAddressRegex = """^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}:\d{1,5}$""".toRegex()

            if (!ledIp.matches(ipAddressRegex)) {
                return false
            }

            val splitAddress = ledIp.split(":")
            val ip = splitAddress[0]

            try {
                InetAddress.getByName(ip)
            } catch (e: Exception) {
                return false
            }

            val port = splitAddress[1].toIntOrNull()
            if (port == null || port !in 1..65535) {
                return false
            }
            return true
        }
    }


}