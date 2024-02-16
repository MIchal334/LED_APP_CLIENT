package com.example.led_app.addapters.outbound

import androidx.room.Dao
import androidx.room.Query
import com.example.led_app.application.ports.outbound.LedAppRepository

@Dao
interface LedAppSqlRepository  : LedAppRepository {

    @Query("SELECT * FROM led_data")
    override fun getAllKnownServerNameAddress(): List<Pair<String, String>>
//    override fun saveNewLed(ledToSave: LedData): Boolean {
//
//    }
//
//    override fun getChangeModeByLedName(ledName: String): List<ChangeModeData> {
//
//    }
//
//    override fun getModeByLedName(ledName: String): List<LedModeData> {
//
//    }
//
//    override fun deleteLed(ledName: String) {
//
//    }
//
//    override fun updateLed(ledData: LedData): Boolean {
//
//    }


}