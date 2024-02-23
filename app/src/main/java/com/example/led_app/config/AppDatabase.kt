
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.Led
import com.example.led_app.domain.LedModeData

@Database(entities = [Led::class, ChangeModeData::class, LedModeData::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ledDao(): LedDao
}