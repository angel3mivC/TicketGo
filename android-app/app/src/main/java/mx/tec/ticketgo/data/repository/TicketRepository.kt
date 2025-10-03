package mx.tec.ticketgo.data.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class TicketRepository(context: Context) {
    private val queue = Volley.newRequestQueue(context)
    private val url = "http://apiticketgo-env.eba-fbhyvbpr.us-east-1.elasticbeanstalk.com"

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
        val url = "$url/tickets/"

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

    fun getTickets(
        estado: String? = null,
        prioridad: String? = null,
        tecnico: String? = null,
        categoria: String? = null,
        fechaInicio: String? = null,
        fechaFin: String? = null,
        callback: (JSONArray?) -> Unit
    ) {
        // Base URL
        val baseUrl = "${url}/tickets/"

        // Construimos los query params dinámicamente
        val queryParams = mutableListOf<String>()

        estado?.let { queryParams.add("estado=$it") }
        prioridad?.let { queryParams.add("prioridad=$it") }
        tecnico?.let { queryParams.add("tecnico=$it") }
        categoria?.let { queryParams.add("categoria=$it") }
        if (fechaInicio != null && fechaFin != null) {
            queryParams.add("fecha_inicio=$fechaInicio")
            queryParams.add("fecha_fin=$fechaFin")
        }

        // Construir URL final
        val finalUrl = if (queryParams.isNotEmpty()) {
            "$baseUrl?${queryParams.joinToString("&")}"
        } else {
            baseUrl
        }

        // Petición GET con JsonArrayRequest
        val request = JsonArrayRequest(
            Request.Method.GET,
            finalUrl,  // <-- Usa finalUrl en lugar de url
            null,
            { response ->
                callback(response)
            },
            { error ->
                error.printStackTrace()
                callback(null)
            }
        )

        queue.add(request)
    }

    fun parseTickets(jsonArray: JSONArray): List<Ticket> {
        val tickets = mutableListOf<Ticket>()

        for (i in 0 until jsonArray.length()) {
            try {
                val jsonObject = jsonArray.getJSONObject(i)

                val ticket = Ticket(
                    ticketId = jsonObject.getInt("ticket_id"),
                    title = jsonObject.getString("title"),
                    description = jsonObject.getString("description"),
                    categoryId = jsonObject.getInt("category_id"),
                    priorityId = jsonObject.getInt("priority_id"),
                    status = jsonObject.getString("status"),
                    tecnico = jsonObject.optString("tecnico", null)
                )

                tickets.add(ticket)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return tickets
    }
}
