package mx.tec.ticketgo.ui.screens.Ticket

import android.content.Context
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.ui.components.BodyText
import mx.tec.ticketgo.ui.components.ErrorMessage
import mx.tec.ticketgo.ui.components.FilterButton
import mx.tec.ticketgo.ui.components.TicketAdmin
import mx.tec.ticketgo.ui.components.TicketAdminPreview
import mx.tec.ticketgo.ui.components.Title
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun AdminTicketScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController, isHistory: Boolean) {

    // 1. Obtener contexto
    val context = LocalContext.current

    // 2. Estados
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val message by ticketViewModel.message.collectAsStateWithLifecycle()

    // 3. Llamar tickets (asegúrate que no se llame en cada recomposición)

    if(isHistory) {
        ticketViewModel.getTickets(state = "Cerrado")
    }
    else{
        ticketViewModel.getTickets()
    }

    // 🔹 Vista lista de tickets o vacía
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
            Title("Tickets")
            message?.let {
                ErrorMessage(it)
            }

            FilterButton(
                onClick = {
                    // TODO: Implementar funcionalidad de filtro
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            tickets.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BodyText("No hay tickets disponibles")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre items
                ) {
                    items(tickets.size) { index ->
                        val ticket = tickets[index]
                        TicketAdminPreview(ticket) { clicked ->
                            // Navegar a la pantalla de detalle con el ID del ticket
                            navController.navigate("adminTicketDetail/${clicked.id_ticket}")
                        }
                    }
                }
            }
        }
    }
}