package mx.tec.ticketgo.data.utils

import retrofit2.Response

fun <T> Response<T>.toResult(): Result<T> {
    return if (this.isSuccessful) {
        val body = this.body()
        if (body != null) {
            Result.success(body)
        } else {
            Result.failure(Exception("Empty response"))
        }
    } else {
        val error = this.errorBody()?.string() ?: "Unknown error"
        Result.failure(Exception(error))
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        response.toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }
}
