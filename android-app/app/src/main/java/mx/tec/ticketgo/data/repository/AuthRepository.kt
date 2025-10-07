package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.ApiClient
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.models.LoginResponse
import mx.tec.ticketgo.data.remote.AuthService

class AuthRepository(){

    private val service: AuthService = ApiClient.retrofit.create(AuthService::class.java)

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = service.login(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val error = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(error))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<GenericResponse> {
        return try {
            val response = service.logout()

            if(response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Sin respuesta"))
                }
            } else {
                val error = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(error))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}