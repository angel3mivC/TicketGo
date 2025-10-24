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
import mx.tec.ticketgo.ui.viewmodels.FileViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun TecnicoTicketDetailScreen(
    ticketId: Int,
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    fileViewModel: FileViewModel,
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
                .padding(16.dp)
        ) {
            rememberScrollState().let { scrollState ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    TicketTecnico(
                        ticket = ticket,
                        comments = comments,
                        onSendComment = { commentText ->
                            commentViewModel.addLocalComment(ticket.id_ticket, commentText)
                        },
                        onAddEvidence = {
                            navController.navigate("fileUpload/$ticketId")
                        },
                        onViewGallery = {
                            navController.navigate("gallery/${ticket.id_ticket}")
                        },
                        onStatusChange = { newStatus, stateId ->
                            // Actualizar estado localmente y hacer llamada a API
                            ticketViewModel.updateTicketStatusLocally(ticket.id_ticket, newStatus)
                            ticketViewModel.changeTicketState(ticket.id_ticket, stateId)
                        }
                    )

                    // 🔹 Botones de Aceptar/Rechazar FUERA del componente del ticket
                    println("🎫 Ticket ${ticket.id_ticket} - aceptado: ${ticket.aceptado}")
                    if (ticket.aceptado == null) {
                        println("📌 Mostrando botones de Aceptar/Rechazar")
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AcceptButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    println("🟢 Botón Aceptar presionado")
                                    ticketViewModel.acceptTicket(ticket.id_ticket, true)
                                }
                            )
                            RejectButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    println("🔴 Botón Rechazar presionado")
                                    ticketViewModel.acceptTicket(
                                        id = ticket.id_ticket,
                                        accepted = false,
                                        onRejected = {
                                            // Navegar hacia atrás cuando se rechaza
                                            println("⬅️ Navegando hacia atrás después de rechazar")
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            )
                        }
                    } else {
                        println("✓ Ticket ya ${if (ticket.aceptado == 1) "aceptado" else "rechazado"}")
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
