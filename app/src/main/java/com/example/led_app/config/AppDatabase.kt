package com.example.led_app.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.led_app.addapters.outbound.LedAppSqlRepository
import com.example.led_app.domain.LedData

@Database(entities = [LedData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ledRepository(): LedAppSqlRepository

}
