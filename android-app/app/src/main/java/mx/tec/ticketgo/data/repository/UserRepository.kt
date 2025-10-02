package mx.tec.ticketgo.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class UserRepository(context: Context) {
    private val queue = Volley.newRequestQueue(context)

    fun createUser(
        name: String,
        email: String,
        password: String,
        role: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ){
        val url = ""

        val jsonBody = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("role", role)
            put("state", "")
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                try{
                    onSuccess(response.getString("message"))
                }catch (e: Exception){
                    onError("${e.message}")
                }
            },
            { error ->
                onError("${error.message}")
            }
        )

        queue.add(request)
    }

    fun editUser(
        userId: Int,
        name: String,
        email: String,
        password: String,
        roleId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ){
        val url = "$userId"

        val jsonBody = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("role_id", roleId)
            put("state", "")
        }

        val request = JsonObjectRequest(
            Request.Method.PUT,
            url,
            jsonBody,
            { response ->
                try{
                    onSuccess("${response.getString("message")}")
                }catch (e: Error){
                    onError("${e.message}")
                }
            },
            { error ->
                onError("${error.message}")
            }
        )

        queue.add(request)
    }
}