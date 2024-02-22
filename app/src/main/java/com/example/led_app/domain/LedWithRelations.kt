package com.example.led_app.domain

import androidx.room.Embedded
import androidx.room.Relation

data class LedWithRelations(
    @Embedded val led: Led,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ledId"
    )
    val ledModes: List<LedModeData>,

    @Relation(
        parentColumn = "uid",
        entityColumn = "ledId"
    )
    val changeModes: List<ChangeModeData>
) {
    companion object {
        fun buildBaseLedData(ledData: LedData): LedWithRelations {
            return LedWithRelations(
                Led.buildBaseOnLedData(ledData.ledName, ledData.ipAddress),
                ledData.ledModes, ledData.changeModes
            )
        }
    }
}