package com.example.led_app.domain

import androidx.room.Embedded
import androidx.room.Relation

data class LedWithRelations(
    @Embedded val led: LedTest,
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
)