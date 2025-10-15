package mx.tec.ticketgo.ui.screens.Ticket

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.ui.components.*
import mx.tec.ticketgo.ui.components.FilterButton
import mx.tec.ticketgo.ui.components.HistoryButton
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun TecnicoTicketScreen(
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    navController: NavController,
    isHistory: Boolean = false
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("id_user", -1)

    // Estados de viewmodels
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()

    // Cargar tickets dependiendo si es historial o activos
    LaunchedEffect(userId, isHistory) {
        if (userId != -1) {
            if (isHistory) {
                ticketViewModel.getTickets(technician = userId, state = "Cerrado")
            } else {
                ticketViewModel.getTickets(technician = userId)
            }
        }
    }

    // Vista lista de tickets
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
            Title(if (isHistory) "Historial de tickets" else "Mis tickets")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterButton(
                    onClick = {
                        // TODO: Implementar funcionalidad de filtro
                    }
                )
                HistoryButton(
                    onClick = {
                        // TODO: Implementar navegación a historial
                    }
                )
            }
        }

        BodyText(if (isHistory) "Consulta los tickets cerrados." else "Consulta todos los tickets asignados.")

        Spacer(modifier = Modifier.height(16.dp))

        when {
            tickets.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay tickets disponibles")
                }
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tickets.size) { index ->
                        val ticket = tickets[index]
                        TicketTecnicoPreview(ticket = ticket) {
                            // Navegar a la pantalla de detalle con el ID del ticket
                            navController.navigate("tecnicoTicketDetail/${ticket.id_ticket}")
                        }
                    }
                }
            }
        }
    }
}
