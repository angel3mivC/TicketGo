package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.models.LoginResponse
import mx.tec.ticketgo.data.remote.AuthService
import mx.tec.ticketgo.data.utils.safeApiCall

class AuthRepository(){
    private val service: AuthService = ApiClient.retrofit.create(AuthService::class.java)

    suspend fun login(request: LoginRequest): Result<LoginResponse> =
        safeApiCall { service.login(request) }

    suspend fun logout(): Result<GenericResponse>  =
        safeApiCall { service.logout() }
}