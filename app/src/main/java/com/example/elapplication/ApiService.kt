// ApiService.kt

import com.example.elapplication.Facture
import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(val phone_number: String, val password: String)
data class LoginResponse(val success: Boolean)

data class SignupRequest(val cin: String, val username: String, val phone_number: String, val email: String, val password: String)
data class SignupResponse(val success: Boolean)

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @GET("factures/unpaid")
    suspend fun getUnpaidBills(
        @Query("phoneNumber") phoneNumber: String? = null,
        @Query("billNumber") billNumber: String? = null
    ): List<Facture>

    @PUT("update")
    suspend fun updateFacture(
        @Query("numFacture") numFacture: String,
        @Query("datePayment") datePayment: String
    ): Facture


    @POST("history")
    fun getFactureHistory(
        @Body request: Map<String, String>
    ): Call<List<Facture>>
}
