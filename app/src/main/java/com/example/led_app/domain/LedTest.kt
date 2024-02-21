package com.example.led_app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LedTest(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "led_name") val ledName: String?,
    @ColumnInfo(name = "ip_address") val ipAddress: String?,
) {
    companion object {
        fun buildBaseOnServerResponse(): LedTest {
            return LedTest(0, "test", "test")
        }
    }

}