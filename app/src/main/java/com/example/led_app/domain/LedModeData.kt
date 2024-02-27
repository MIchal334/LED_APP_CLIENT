package com.example.led_app.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "led_mode_data", foreignKeys = [
    ForeignKey(
        entity = Led::class,
        parentColumns = ["uid"],
        childColumns = ["ledId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class LedModeData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var ledId: Long,
    val optionName: String,
    val modeServerId: Int,
    val setColor: Boolean? = false
)

