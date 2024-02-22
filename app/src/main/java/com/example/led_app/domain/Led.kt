package com.example.led_app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "led")
data class Led(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "led_name") val ledName: String?,
    @ColumnInfo(name = "ip_address") val ipAddress: String?,
) {
    companion object {
        fun buildBaseOnLedData(ledName:String, ipAddress:String): Led {
            return Led(0, ledName, ipAddress)
        }
    }

}