package mx.tec.ticketgo.ui.screens.Ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun AdminTicketDetailScreen(
    ticketId: Int,
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    navController: NavController
) {
    // ViewModels
    val userViewModel = remember { UserViewModel() }
    
    // Estados del modal
    var showAssignTechnicianModal by remember { mutableStateOf(false) }
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()
    
    // Obtener el ticket específico
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val ticket = tickets.find { it.id_ticket == ticketId }
    
    // Obtener técnicos para poder acceder a sus nombres
    val technicians by userViewModel.technicians.collectAsStateWithLifecycle()
    
    // Obtener comentarios del ticket y cargar técnicos
    LaunchedEffect(ticketId) {
        commentViewModel.getComments(ticketId)
        userViewModel.getTechnicians() // Cargar técnicos específicamente
    }

    if (ticket != null) {
        Column(
            modifier = Modifier
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
                    TicketAdmin(
                        ticket = ticket,
                        comments = comments,
                        onSendComment = { commentText ->
                            commentViewModel.addLocalComment(ticket.id_ticket, commentText)
                        },
                        onViewGallery = {
                            navController.navigate("gallery/${ticket.id_ticket}")
                        },
                        onStatusChange = { newStatus, stateId ->
                            // Actualizar estado localmente y hacer llamada a API
                            ticketViewModel.updateTicketStatusLocally(ticket.id_ticket, newStatus)
                            ticketViewModel.changeTicketState(ticket.id_ticket, stateId)
                        },
                        onAssignTechnician = {
                            showAssignTechnicianModal = true
                        }
                    )
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
    
    // Modal para asignar técnico
    AssignTechnicianModal(
        isVisible = showAssignTechnicianModal,
        onDismiss = { showAssignTechnicianModal = false },
        onAssign = { technicianId ->
            // Obtener el nombre del técnico antes de asignar
            val technician = technicians.find { it.id_usuario == technicianId }
            val technicianName = technician?.nombre
            
            // Actualizar localmente con el nombre del técnico
            ticketViewModel.updateTicketTechnicianLocally(ticketId, technicianId, technicianName)
            
            // Llamar a la API
            ticketViewModel.assignTicket(ticketId, technicianId, technicianName)
            showAssignTechnicianModal = false
        },
        userViewModel = userViewModel
    )
}
