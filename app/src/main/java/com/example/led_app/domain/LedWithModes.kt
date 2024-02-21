package com.example.led_app.domain

import androidx.room.Embedded
import androidx.room.Relation

data class LedWithModes (
    @Embedded val led: LedTest,
    @Relation(
        parentColumn = "uid",
        entityColumn = "ledId"
    )
    val modes: List<LedModeData>
)