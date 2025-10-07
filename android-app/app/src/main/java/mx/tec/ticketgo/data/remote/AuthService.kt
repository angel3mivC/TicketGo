package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<GenericResponse>
}