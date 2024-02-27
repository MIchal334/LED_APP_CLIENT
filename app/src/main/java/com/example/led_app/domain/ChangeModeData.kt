package com.example.led_app.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "led_change_data", foreignKeys = [
    ForeignKey(
        entity = Led::class,
        parentColumns = ["uid"],
        childColumns = ["ledId"],
        onDelete = ForeignKey.CASCADE
    )
])
class ChangeModeData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var ledId: Long,
    val optionName: String,
    val changeModeServerId: Int
) {

}

