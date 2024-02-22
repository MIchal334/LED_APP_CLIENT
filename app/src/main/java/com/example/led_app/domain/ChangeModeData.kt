package com.example.led_app.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "led_change_data")
data class ChangeModeData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ledId: Int,
    val optionName: String,
    val changeModeServerId: Int
)

