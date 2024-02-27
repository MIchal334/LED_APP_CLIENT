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

    @Transaction
    @Query("Select * FROM led_change_data")
    fun getAll(): List<ChangeModeData>

    @Transaction
    @Query("Select * FROM led_mode_data")
    fun getAll1(): List<LedModeData>

    @Transaction
    @Query("Delete FROM led_change_data")
    fun deleteAll()

    @Transaction
    @Query("Delete FROM led_mode_data")
    fun deleteAll1()
    override fun getAllKnownServerNameAddress(): List<Pair<String, String>> {
//        deleteAll()
//        deleteAll1()
        deleteLed()
        var test = getAll()
        var test1 = getAll1()
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
        return getAllLed().first { led -> led.led.ledName.equals(ledName) }.changeModes
    }

    override fun getModeByLedName(ledName: String): List<LedModeData> {
        return getAllLed().first { led -> led.led.ledName.equals(ledName) }.ledModes
    }

    override fun deleteLed(ledName: String) {
        TODO("Not yet implemented")
    }

    override fun updateLed(ledData: LedData): Boolean {
        TODO("Not yet implemented")
    }

    @Transaction
    @Query("DELETE FROM led")
    fun deleteLed()

//    @Delete
//    fun deleteModes(modes: List<LedModeData>)
//
//    @Delete
//    fun deleteAnotherEntities(anotherEntities: List<ChangeModeData>)

    @Insert
    fun insertLed(led: Led): Long

    @Insert
    fun insertModes(modes: List<LedModeData>)

    @Insert
    fun insertAnotherEntities(anotherEntities: List<ChangeModeData>)
}