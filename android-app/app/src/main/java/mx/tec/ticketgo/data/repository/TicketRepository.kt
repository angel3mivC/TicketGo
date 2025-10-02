package mx.tec.ticketgo.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class TicketRepository(context: Context) {
    private val queue = Volley.newRequestQueue(context)

    fun createTicket(
        title: String,
        description: String,
        priorityId: Int,
        categoryId: Int,
        status: String,
        comments: String,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ){
        val url = "http://apiticketgo-env.eba-fbhyvbpr.us-east-1.elasticbeanstalk.com/tickets/"

        val jsonBody = JSONObject().apply {
            put("title", title)
            put("description", description)
            put("category_id", categoryId)
            put("priority_id", priorityId)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                try{
                    onSuccess(response.getInt("ticket_id"))
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
}