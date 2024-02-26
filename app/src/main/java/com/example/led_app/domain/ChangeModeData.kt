package com.example.led_app.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "led_change_data")
class ChangeModeData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var ledId: Long,
    val optionName: String,
    val changeModeServerId: Int
) {

}

