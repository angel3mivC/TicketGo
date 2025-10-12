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
import androidx.compose.material3.Scaffold
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
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.ErrorMessage
import mx.tec.ticketgo.ui.components.TicketAdminPreview
import mx.tec.ticketgo.ui.components.TicketTecnico
import mx.tec.ticketgo.ui.components.Title
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel


@Composable
fun AdminTicketScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController) {

    var selectedTicket by remember { mutableStateOf<Ticket?>(null) }

    // 1. Obtener contexto
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // 2. Estados
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()
    val message by ticketViewModel.message.collectAsStateWithLifecycle()

    // 3. Llamar tickets (asegúrate que no se llame en cada recomposición)
    ticketViewModel.getTickets()

    // 🔹 Vista detalle del ticket
    if (selectedTicket != null) {
        val ticket = selectedTicket!!
        commentViewModel.getComments(ticket.id_ticket)

        Scaffold(
            topBar = {
                TopBar(
                    title = "Detalle del ticket",
                    navController = navController,
                    onBack = { selectedTicket = null } // 🔙 Regresar a la lista
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.rememberScrollState().let { scrollState ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        TicketTecnico(ticket = ticket, comments = comments)
                    }
                }
            }
        }

    } else {
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

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("Filtrar", Color.Gray)
                }
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(tickets.size) { index ->
                            val ticket = tickets[index]
                            TicketAdminPreview(ticket) { clicked ->
                                selectedTicket = clicked
                            }
                        }
                    }
                }
            }
        }
    }
}