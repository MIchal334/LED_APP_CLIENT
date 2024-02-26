package com.example.led_app.addapters.outbound

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.led_app.application.ports.outbound.LedAppRepository
import com.example.led_app.domain.*

@Dao
interface LedDao : LedAppRepository {
    @Transaction
    @Query("SELECT * FROM led")
    fun getAllLed(): List<LedWithRelations>
    override fun getAllKnownServerNameAddress(): List<Pair<String, String>> {
        return getAllLed().map { led -> Pair(led.led.ledName!!, led.led.ipAddress!!) }.toList()
    }


    @Transaction
    override fun saveNewLed(ledToSave: LedData): Boolean {
        val ledWithRelations = LedWithRelations.buildBaseLedData(ledToSave)
        val identifier = insertLed(ledWithRelations.led)
        ledWithRelations.ledModes.forEach { el -> el.ledId = identifier }
        ledWithRelations.changeModes.forEach { el -> el.ledId = identifier }
        insertModes(ledWithRelations.ledModes)
        insertAnotherEntities(ledWithRelations.changeModes)
        return true
    }

    override fun getChangeModeByLedName(ledName: String): List<ChangeModeData> {
        TODO("Not yet implemented")
    }

    override fun getModeByLedName(ledName: String): List<LedModeData> {
        TODO("Not yet implemented")
    }

    override fun deleteLed(ledName: String) {
        TODO("Not yet implemented")
    }

    override fun updateLed(ledData: LedData): Boolean {
        TODO("Not yet implemented")
    }


    @Insert
    fun insertLed(led: Led): Long

    @Insert
    fun insertModes(modes: List<LedModeData>)

    @Insert
    fun insertAnotherEntities(anotherEntities: List<ChangeModeData>)
}