package mx.tec.ticketgo.data.utils

import retrofit2.Response
import android.util.Log

fun <T> Response<T>.toResult(): Result<T> {
    return if (this.isSuccessful) {
        val body = this.body()
        if (body != null) {
            Log.i("API_RESPONSE", "✅ Respuesta exitosa: $body")
            Result.success(body)
        } else {
            Log.e("API_RESPONSE", "❌ Respuesta vacía")
            Result.failure(Exception("Empty response"))
        }
    } else {
        val errorCode = this.code()
        val errorMessage = this.message()
        val errorBody = this.errorBody()?.string() ?: "Unknown error"
        
        Log.e("API_RESPONSE", "❌ Error HTTP $errorCode: $errorMessage")
        Log.e("API_RESPONSE", "❌ Error Body: $errorBody")
        
        Result.failure(Exception("HTTP $errorCode: $errorMessage - $errorBody"))
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        Log.i("API_CALL", "🚀 Iniciando llamada a API")
        val response = apiCall()
        Log.i("API_CALL", "📡 Respuesta recibida: ${response.code()}")
        response.toResult()
    } catch (e: Exception) {
        Log.e("API_CALL", "💥 Excepción en llamada API: ${e.message}", e)
        Result.failure(e)
    }
}
