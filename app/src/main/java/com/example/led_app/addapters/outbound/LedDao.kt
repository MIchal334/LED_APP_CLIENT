import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.example.led_app.application.ports.outbound.LedAppRepository
import com.example.led_app.domain.*

@Dao
interface LedDao : LedAppRepository {

    override fun getAllKnownServerNameAddress(): List<Pair<String, String>> {
        val resultList = mutableListOf<Pair<String, String>>()
        return resultList
    }


    @Transaction
    override fun saveNewLed(ledToSave: LedData): Boolean {
        val ledWithRelations = LedWithRelations.buildBaseLedData(ledToSave)
        insertLedTest(ledWithRelations.led)
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
    fun insertLedTest(led: Led)

    @Insert
    fun insertModes(modes: List<LedModeData>)

    @Insert
    fun insertAnotherEntities(anotherEntities: List<ChangeModeData>)
}