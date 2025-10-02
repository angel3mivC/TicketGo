package mx.tec.ticketgo.ui.screens.Ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.TicketTecnicoPreview
import mx.tec.ticketgo.ui.components.Title
import org.json.JSONArray
import org.json.JSONObject

val tickets = JSONArray().apply {
    put(JSONObject().apply {
        put("id", "T12")
        put("fechaHora", "2 sept 2023 8:35 am")
        put("descripcion", "Descripción. Lorem Ipsum Lorem Impsum")
        put("categoria", "Garantia")
        put("estado", "Abierto")
        put("prioridad", "Alta")
    })
    put(JSONObject().apply {
        put("id", "T13")
        put("fechaHora", "14 oct 2023 8:55 am")
        put("descripcion", "Descripción. Lorem Ipsum Lorem Impsum Lorem Ipsum")
        put("categoria", "Falla")
        put("estado", "En proceso")
        put("prioridad", "Media")
    })
    put(JSONObject().apply {
        put("id", "T14")
        put("fechaHora", "30 oct 2023 10:15 am")
        put("descripcion", "Descripción. Lorem Ipsum Lorem Impsum Lorem Ipsum Lorem Ipsum")
        put("categoria", "Falla")
        put("estado", "Abierto")
        put("prioridad", "Baja")
    })
}

@Composable
fun TecnicoTicketScreen(tickets: JSONArray){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Title("Mis tickets")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))  {
                Chip("Filtrar",Color.Gray)
                Chip("Historial", Color.Gray)
            }

        }
        for (i in 0 until tickets.length()) {
            val ticket = tickets.getJSONObject(i)

            TicketTecnicoPreview(
                ticket.getString("id"),
                ticket.getString("fechaHora"),
                ticket.getString("descripcion"),
                ticket.getString("categoria"),
                ticket.getString("estado"),
                ticket.getString("prioridad")
            )
        }
    }
}

@Preview
@Composable
fun TecnicoTicketScreenPreview() {
    TecnicoTicketScreen(tickets)
}