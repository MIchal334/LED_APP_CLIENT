package com.example.led_app.domain

import ServerResponse
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "led_data")
class LedData(
    val ledName: String = "A",
    val ipAddress: String = "127.0.0.1",
    @Embedded val ledModes: List<LedModeData> = listOf(LedModeData("rain", 1)),
    @Embedded val changeModes: List<ChangeModeData> = listOf(ChangeModeData("snake", 1))
) {
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0

    companion object {
        fun buildBaseOnServerResponse(serverResponse: ServerResponse, name: String, ipAddress: String): LedData {
            return LedData(name, ipAddress, serverResponse.ledModes, serverResponse.changeModes)
        }
    }

}


