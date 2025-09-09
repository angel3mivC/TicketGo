package mx.tec.ticketgo.data.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AuthRepository(private val context: Context){
    private val queue = Volley.newRequestQueue(context)

    fun Login(
        username: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
     ){
        val URL = "http://10.49.179.103:3000/auth/login"

        val params = JSONObject().apply {
            put("correo", username)
            put("contraseña", password)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            URL,
            params,
            { response ->
                try{
                    val token = response.getString("token")
                    onSuccess(token)
                }catch (e: Exception){
                    onError("Error parsing response: ${e.message}")
                }
            },
            { error ->
                Log.e("VolleyError", error.message ?: "Unknown error")
                onError(error.message ?: "Unknown error")

            }
        )

        queue.add(request)
    }
}