package mx.tec.ticketgo.data.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AuthRepository(context: Context){
    private val queue = Volley.newRequestQueue(context)

    fun login(
        username: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
     ){
        val url = "http://192.168.56.1:3000/auth/login"

        val jsonBody = JSONObject().apply {
            put("correo", username)
            put("contraseña", password)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                try{
                    val token = response.getString("token")
                    onSuccess(token)
                }catch (e: Exception){
                    onError("Error parsing response: ${e.message}")
                }
            },
            { error ->
                val errorMessage = when {
                    error.networkResponse != null -> {
                        "Status: ${error.networkResponse.statusCode}, " +
                                "Data: ${String(error.networkResponse.data)}"
                    }
                    error.cause != null -> "Cause: ${error.cause?.message}"
                    else -> "Network error: ${error.message}"
                }
                Log.e("VolleyError", errorMessage)
                onError(errorMessage)
            }
        )

        queue.add(request)
    }
}