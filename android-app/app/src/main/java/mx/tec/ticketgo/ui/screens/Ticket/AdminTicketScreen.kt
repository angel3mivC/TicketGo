package mx.tec.ticketgo.ui.screens.Ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.ui.components.BodyText
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.TicketAdminPreview
import mx.tec.ticketgo.ui.components.Title


@Composable
fun AdminTicketScreen() {
    val context = LocalContext.current


    var tickets by remember { mutableStateOf<List<Ticket>>(emptyList()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Title("Mis tickets")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Chip("Filtrar", Color.Gray)
                Chip("Historial", Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {

            tickets.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BodyText("No hay tickets disponibles")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = tickets.size
                    ) { index ->
                        val ticket = tickets[index]
                        TicketAdminPreview(ticket = ticket)
                    }
                }
            }
        }
    }
}

val mockTickets = emptyList<Ticket>()

@Composable
fun TicketScreenPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mockTickets.size) { index ->
                TicketAdminPreview(ticket = mockTickets[index])
            }
        }
    }
}
