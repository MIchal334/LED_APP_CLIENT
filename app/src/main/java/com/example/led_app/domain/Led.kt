package com.example.led_app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "led")
data class Led(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "led_name") val ledName: String?,
    @ColumnInfo(name = "ip_address") val ipAddress: String?,
) {
    companion object {
        fun buildBaseOnLedData(ledName:String, ipAddress:String): Led {
            return Led(ledName =  ledName, ipAddress = ipAddress)
        }
    }

}