// ApiService.kt

import com.example.elapplication.Facture
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginRequest(val phone_number: String, val password: String)
data class LoginResponse(val success: Boolean)

data class SignupRequest(val cin: String, val username: String, val phone_number: String, val email: String, val password: String)
data class SignupResponse(val success: Boolean)

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @GET("facture") // Define the appropriate endpoint
    suspend fun getUnpaidBills(
        @Query("state") state: String,
        @Query("phoneNumber") phoneNumber: String? = null,
        @Query("billNumber") billNumber: String? = null
    ): List<Facture>
}
