import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.led_app.domain.LedData
import com.example.led_app.domain.OptionRequestData
import com.example.led_app.ports.inbound.LedClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.PUT

data class ServerResponse(
    val ledModes: List<OptionRequestData>,
    val changeModes: List<OptionRequestData>
)

interface ApiService {
    @GET("test")
    fun getTest(): Call<Void>

    @GET("config")
    fun getConfig(): Call<ServerResponse>

    @PUT("off")
    fun turnOffLed(): Call<Void>

}

//TODO UJEDNLISIC TO BO SIE FUNKCJNLANOSCI POWTARZAJA ZROBIC JEDNA METODE "doRequest"
class LedClientSimulation : LedClient {

    override suspend fun getTestConnection(ipAddress: String): Boolean {
        return testConnectionRequest(ipAddress)
    }

    override suspend fun getServerConfiguration(
        ledName: String,
        ipAddress: String
    ): Either<RuntimeException, LedData> {
        return getConfigRequest(ledName, ipAddress)
    }

    override suspend fun turnOffLed(ipAddress: String): Boolean {
        return tryTurnOffLed(ipAddress)
    }

    private suspend fun tryTurnOffLed(ipAddress: String): Boolean {
        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(ipAddress).getTest().execute()
            }
            if (response.code() == 204) {
                Log.i("Response from server", "Connection OK")
                return true
            }
            return false
        } catch (e: Exception) {
            Log.i("Response from server", "NOT CONNECTION")
            return false
        }
    }

    private suspend fun testConnectionRequest(ipAddress: String): Boolean {
        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(ipAddress).getTest().execute()
            }
            if (response.code() == 200) {
                Log.i("Response from server", "Connection OK")
                return true
            }
            return false
        } catch (e: Exception) {
            Log.i("Response from server", "NOT CONNECTION")
            return false
        }
    }

    private suspend fun getConfigRequest(ledName: String, ipAddress: String): Either<RuntimeException, LedData> {

        return try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(ipAddress).getConfig().execute()
            }
            if (response.code() == 200) {
                Log.i("Response from server", "Response: ${response.body()}")
                LedData.buildBaseOnServerResponse(
                    name = ledName,
                    ipAddress = ipAddress,
                    serverResponse = response.body()!!
                ).right()

            } else {
                Log.i("Response from server", "Unsuccessful response: ${response.code()}")
                RuntimeException("Not connection").left()
            }
        } catch (e: Exception) {
            Log.e("Response from server", "Request failed: $e", e)
            RuntimeException("Not connection").left()
        }
    }

    private fun prepareRestClient(ipAddress: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(ipAddress + "/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService::class.java)
    }

}
