
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedModeData
import com.example.led_app.domain.LedTest
import com.example.led_app.domain.LedWithRelations

@Dao
interface LedDao {

    @Transaction
    fun saveLed(ledWithRelations: LedWithRelations) {
        insertLedTest(ledWithRelations.led)
        insertModes(ledWithRelations.ledModes)
        insertAnotherEntities(ledWithRelations.changeModes)
    }

    @Insert
    fun insertLedTest(ledTest: LedTest)

    @Insert
    fun insertModes(modes: List<LedModeData>)

    @Insert
    fun insertAnotherEntities(anotherEntities: List<ChangeModeData>)
}