import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.led_app.domain.LedData
import com.example.led_app.domain.OptionRequestData
import com.example.led_app.ports.inbound.LedClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class ServerResponse(
    val ledModes: List<OptionRequestData>,
    val changeModes: List<OptionRequestData>
)

interface ApiService {
    @GET("test")
    fun get(): Call<ServerResponse>
}

class LedClientSimulation : LedClient {
    private val serverAddress: String = "http://localhost:8090/"

    override fun getServerConfiguration(
        ledName: String,
        ipAddress: String
    ): Either<RuntimeException, LedData> {
        return tryConnection(ledName, ipAddress)
    }

    private fun tryConnection(ledName: String, ipAddress: String): Either<RuntimeException, LedData> {

        return try {
            val response = prepareRestClient().get().execute()
            if (response.code() == 200) {
                LedData.buildBaseOnServerResponse(
                    name = ledName,
                    ipAddress = ipAddress,
                    serverResponse = response.body()!!
                ).right()

            } else {
                Log.e("Response from server", "Unsuccessful response: ${response.code()}")
                RuntimeException("Not connection").left()
            }
        } catch (e: Exception) {
            Log.e("Response from server", "Unsuccessful connection: ${e}")
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
