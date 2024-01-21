import android.net.Uri
import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.led_app.application.ports.inbound.LedClient
import com.example.led_app.application.ports.inbound.dto.ColorRequestDto
import com.example.led_app.domain.ChangeModeData
import com.example.led_app.domain.LedData
import com.example.led_app.domain.LedModeData
import com.example.led_app.domain.NewServerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

//TODO TOW OTHER ENTITY
data class ServerResponse(
    val ledModes: List<LedModeData>,
    val changeModes: List<ChangeModeData>
)

interface ApiService {
    @GET("test")
    fun getTest(): Call<Void>

    @GET("config")
    fun getConfig(): Call<ServerResponse>

    @PUT("off")
    fun turnOffLed(): Call<Void>

    @POST("color")
    fun sendNewColorRequest(@Body requestBody: ColorRequestDto): Call<Void>

    @POST("mode")
    fun sendNewModeRequest(@Body requestBody: ColorRequestDto): Call<Void>

}


class LedClientSimulation : LedClient {

    private val httpAddressTemplate = "http://%s/"

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

    override suspend fun sendColorRequest(colorRequest: NewServerRequest): Boolean {
        return trySendColorRequest(colorRequest)
    }

    override suspend fun sendModeRequest(colorRequest: NewServerRequest): Boolean {
        return trySendModeRequest(colorRequest)
    }

    private suspend fun trySendColorRequest(colorRequest: NewServerRequest): Boolean {

        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(Uri.decode(colorRequest.ledIp)).sendNewColorRequest(ColorRequestDto.of(colorRequest))
                    .execute()
            }
            if (response.code() == 201) {
                Log.i("Response from server", "Color request sended.")
                return true
            }
            Log.i("Response from server", "Something went wrong")
            return false
        } catch (e: Exception) {
            Log.i("Response from server", "SEND COLOR REQUEST UNSUCCESFULL. CODE" + e)
            return false
        }

    }


    private suspend fun trySendModeRequest(colorRequest: NewServerRequest): Boolean {

        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(Uri.decode(colorRequest.ledIp)).sendNewModeRequest(ColorRequestDto.of(colorRequest))
                    .execute()
            }
            if (response.code() == 201) {
                Log.i("Response from server", "Mode request sent.")
                return true
            }
            Log.i("Response from server", "Something went wrong")
            return false
        } catch (e: Exception) {
            Log.i("Response from server", "SEND MODE REQUEST UNSUCCESFULL. CODE" + e)
            return false
        }

    }


    private suspend fun tryTurnOffLed(ipAddress: String): Boolean {
        try {
            val response = withContext(Dispatchers.IO) {
                prepareRestClient(ipAddress).turnOffLed().execute()
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
            .baseUrl(httpAddressTemplate.format(ipAddress))
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService::class.java)
    }

}
