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
fun AdminTicketScreen(
    commentViewModel: CommentsViewModel, 
    ticketViewModel: TicketsViewModel, 
    navController: NavController, 
    isHistory: Boolean = false,
    filterSections: List<FilterSection> = emptyList()
) {
    var selectedTicket by remember { mutableStateOf<Ticket?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var currentFilterSections by remember { mutableStateOf(filterSections) }

    // 1. Obtener contexto
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // 2. Estados
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val isLoading by ticketViewModel.isLoading.collectAsStateWithLifecycle()

    // Debug: Mostrar cantidad de tickets recibidos
    LaunchedEffect(tickets) {
        tickets.forEach { ticket ->
            println("  - Ticket ${ticket.id_ticket}: ${ticket.titulo} (Estado: ${ticket.estado})")
        }
    }

    // 3. Llamar tickets usando LaunchedEffect para evitar múltiples llamadas
    LaunchedEffect(isHistory) {
        if(isHistory) {
            ticketViewModel.getTickets(state = "Cerrado")
        }
        else{
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
                FilterButton(
                    onClick = { showFilterSheet = true }
                )
            }
        }

        // Modal de filtros
        if (showFilterSheet && currentFilterSections.isNotEmpty()) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false }
            ) {
                FilterScreen(
                    sections = currentFilterSections,
                    onOptionSelected = { sectionTitle, option, isSelected ->
                        // Actualizar las secciones de filtro - solo una selección por sección
                        val updatedSections = currentFilterSections.map { section ->
                            if (section.title == sectionTitle) {
                                val newSelectedOptions = if (isSelected) {
                                    setOf(option) // Solo una opción seleccionada
                                } else {
                                    emptySet() // Si se deselecciona, quitar todas
                                }
                                section.copy(selectedOptions = newSelectedOptions)
                            } else {
                                section
                            }
                        }
                        
                        // Actualizar el estado local
                        currentFilterSections = updatedSections
                        
                        // Aplicar filtro inmediatamente con el estado actualizado
                        val stateString = updatedSections
                            .find { it.title == "Estado" }
                            ?.selectedOptions
                            ?.firstOrNull()

                        val categoryString = updatedSections
                            .find { it.title == "Categoría" }
                            ?.selectedOptions
                            ?.firstOrNull()

                        if (isHistory) {
                            // En historial solo filtramos por categoría, estado siempre es "Cerrado"
                            ticketViewModel.getTickets(state = "Cerrado", category = categoryString)
                        } else {
                            // En pantalla normal filtramos por estado y categoría
                            ticketViewModel.getTickets(state = stateString, category = categoryString)
                        }
                    },
                    onClose = {
                        showFilterSheet = false
                    }
                )
            }
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