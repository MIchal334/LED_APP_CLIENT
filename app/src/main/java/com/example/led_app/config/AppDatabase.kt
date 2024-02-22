import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedModeData
import com.example.led_app.domain.LedTest

@Database(entities = [LedTest::class, ChangeModeData::class, LedModeData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ledDao(): LedDao
}