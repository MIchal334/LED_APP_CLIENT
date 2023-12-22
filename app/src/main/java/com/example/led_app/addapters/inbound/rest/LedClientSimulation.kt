import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.led_app.domain.LedData
import com.example.led_app.domain.OptionRequestData
import com.example.led_app.ports.inbound.LedClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

data class ServerResponse(
    val ledModes: List<OptionRequestData>,
    val changeModes: List<OptionRequestData>
)

interface ApiService {
    @GET("test")
    fun getTest(): Call<Void>

    @GET("config")
    fun getConfig(): Call<ServerResponse>

    @GET
    suspend fun executeDynamicGetRequest(@Url url: String): Response<Void>

    @POST
    suspend fun executeDynamicPostRequest(@Url url: String, @Body requestBody: RequestBody): Response<Void>
}

class LedClientSimulation : LedClient {
    private val serverAddress: String = "http://192.168.1.8:8090/"
    override suspend fun getTestConnection(ipAddress: String): Boolean {
       return testConnectionRequest(serverAddress)
    }

    override suspend fun getServerConfiguration(
        ledName: String,
        ipAddress: String
    ): Either<RuntimeException, LedData> {
        return getConfigRequest(ledName, ipAddress)
    }

    private suspend fun testConnectionRequest(ipAddress: String): Boolean {
        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient().getTest().execute()
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
                prepareRestClient().getConfig().execute()
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

    private fun prepareRestClient(): ApiService {
        return Retrofit.Builder()
            .baseUrl(serverAddress)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService::class.java)
    }

}
