import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.led_app.domain.LedTest

@Dao
interface LedDao {
    @Query("SELECT * FROM ledtest")
    fun getAll(): List<LedTest>

    @Query("SELECT * FROM ledtest WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<LedTest>

    @Query("SELECT * FROM ledtest WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): LedTest

    @Insert
    fun insertAll(vararg ledTests: LedTest)

    @Delete
    fun delete(ledTest: LedTest)
}