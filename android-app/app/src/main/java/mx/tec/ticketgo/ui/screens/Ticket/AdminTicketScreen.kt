package mx.tec.ticketgo.ui.screens.Ticket

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import mx.tec.ticketgo.ui.components.FilterButton
import mx.tec.ticketgo.ui.components.TicketAdmin
import mx.tec.ticketgo.ui.components.TicketAdminPreview
import mx.tec.ticketgo.ui.components.Title
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.screens.filters.FilterScreen
import mx.tec.ticketgo.ui.screens.filters.FilterSection
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTicketScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController,  filterSections: List<FilterSection>) {

    var selectedTicket by remember { mutableStateOf<Ticket?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) } //Estado para controlar si se abre la sección de filtros

    // 1. Obtener contexto
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // 2. Estados
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val isLoading by ticketViewModel.isLoading.collectAsStateWithLifecycle()

    // Debug: Mostrar cantidad de tickets recibidos
    LaunchedEffect(tickets) {
        println("📊 Tickets recibidos: ${tickets.size}")
        tickets.forEach { ticket ->
            println("  - Ticket ${ticket.id_ticket}: ${ticket.titulo} (Estado: ${ticket.estado})")
        }
    }

    // 3. Llamar tickets usando LaunchedEffect para evitar múltiples llamadas
    LaunchedEffect(isHistory) {
        if(isHistory) {
            println("🔍 Cargando tickets de historial para admin con estado 'Cerrado'")
            ticketViewModel.getTickets(state = "Cerrado")
        }
        else{
            println("🔍 Cargando tickets activos para admin")
            ticketViewModel.getTickets()
        }
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

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { showFilterSheet = true }) {
                        Text("Filtrar")
                    }
                    if (showFilterSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showFilterSheet = false }
                        ) {
                            FilterScreen(
                                sections = filterSections,
                                onOptionSelected = { sectionTitle, option, isSelected ->
                                },
                                onClose = {
                                    val stateId = filterSections
                                        .find { it.title == "Estado" }
                                        ?.selectedOptions
                                        ?.firstOrNull()
                                        ?.let { option ->
                                            when (option) {
                                                "Abierto" -> 1
                                                "En progreso" -> 2
                                                "Cerrado" -> 3
                                                "Resuelto" -> 4
                                                "Reabierto" -> 5
                                                else -> null
                                            }
                                        }

                                    val categoryId = filterSections
                                        .find { it.title == "Categoría" }
                                        ?.selectedOptions
                                        ?.firstOrNull()
                                        ?.let { option ->
                                            when (option) {
                                                "En proceso" -> 1
                                                "Daño Inducido" -> 2
                                                "Garantía" -> 3
                                                else -> null
                                            }
                                        }

                                    ticketViewModel.getTickets(state = stateId, category = categoryId)

                                    showFilterSheet = false
                                }
                            )
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading && tickets.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

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
                    verticalArrangement = Arrangement.spacedBy(12.dp), // Espacio entre items
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(tickets.size) { index ->
                        val ticket = tickets[index]
                        TicketAdminPreview(ticket) { clicked ->
                            // Navegar a la pantalla de detalle con el ID del ticket
                            if (isHistory) {
                                navController.navigate("adminTicketDetailHistorial/${clicked.id_ticket}")
                            } else {
                                navController.navigate("adminTicketDetail/${clicked.id_ticket}")
                            }
                        }
                    }
                }
            }
        }
    }
}