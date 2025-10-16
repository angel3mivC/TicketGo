package mx.tec.ticketgo.ui.screens.Ticket

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.components.TicketAdminHistorial
import mx.tec.ticketgo.ui.components.TicketTecnicoHistorial
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun TecnicoTicketDetailHistorialScreen(
    ticketId: Int,
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    navController: NavController
) {
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()
    
    // Obtener el ticket específico
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val ticket = tickets.find { it.id_ticket == ticketId }
    
    // Obtener comentarios del ticket
    LaunchedEffect(ticketId) {
        commentViewModel.getComments(ticketId)
    }

    if (ticket != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // TopBar para volver
            TopBar(
                title = "Detalle del ticket",
                onBack = { navController.navigateUp() }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
            rememberScrollState().let { scrollState ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    TicketTecnicoHistorial(
                        ticket = ticket,
                        comments = comments,
                        onViewGallery = {
                            navController.navigate("gallery/${ticket.id_ticket}")
                        }
                    )
                }
            }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ticket no encontrado")
        }
    }
}

@Composable
fun AdminTicketDetailHistorialScreen(
    ticketId: Int,
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    navController: NavController
) {
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()
    
    // Obtener el ticket específico
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val ticket = tickets.find { it.id_ticket == ticketId }
    
    // Obtener comentarios del ticket
    LaunchedEffect(ticketId) {
        commentViewModel.getComments(ticketId)
    }

    if (ticket != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // TopBar para volver
            TopBar(
                title = "Detalle del ticket",
                onBack = { navController.navigateUp() }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
            androidx.compose.foundation.rememberScrollState().let { scrollState ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    TicketAdminHistorial(
                        ticket = ticket,
                        comments = comments,
                        onViewGallery = {
                            navController.navigate("gallery/${ticket.id_ticket}")
                        }
                    )
                }
            }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ticket no encontrado")
        }
    }
}
