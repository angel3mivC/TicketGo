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
import androidx.compose.material.icons.filled.Home
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
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.screens.filters.FilterSection
import mx.tec.ticketgo.ui.screens.filters.FilterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoTicketScreen(
    commentViewModel: CommentsViewModel,
    ticketViewModel: TicketsViewModel,
    navController: NavController,
    isHistory: Boolean = false,
    filterSections: List<FilterSection> = emptyList()
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("id_user", -1)

    // Estados para filtros
    var showFilterSheet by remember { mutableStateOf(false) }
    var currentFilterSections by remember { mutableStateOf(filterSections) }

    // Estados de viewmodels
    val tickets by ticketViewModel.tickets.collectAsStateWithLifecycle()
    val comments by commentViewModel.comment.collectAsStateWithLifecycle()
    val isLoading by ticketViewModel.isLoading.collectAsStateWithLifecycle()

    // Debug: Mostrar cantidad de tickets recibidos
    LaunchedEffect(tickets) {
        tickets.forEach { ticket ->
            println("  - Ticket ${ticket.id_ticket}: ${ticket.titulo} (Estado: ${ticket.estado})")
        }
    }

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
                        if (currentFilterSections.isNotEmpty()) {
                            showFilterSheet = true
                        }
                    }
                )
                if (isHistory) {
                    // Botón de home cuando estamos en historial
                    IconButton(
                        onClick = {
                            navController.navigate("tecnicoHome")
                        },
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = Color.Black,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                    }
                } else {
                    // Botón de historial cuando estamos en home
                    HistoryButton(
                        onClick = {
                            navController.navigate("tecnicoTicketHistory")
                        }
                    )
                }
            }
        }

        BodyText(if (isHistory) "Consulta los tickets cerrados." else "Consulta todos los tickets asignados.")

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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay tickets disponibles")
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(tickets.size) { index ->
                        val ticket = tickets[index]
                        TicketTecnicoPreview(ticket = ticket) {
                            // Navegar a la pantalla de detalle con el ID del ticket
                            if (isHistory) {
                                navController.navigate("tecnicoTicketDetailHistorial/${ticket.id_ticket}")
                            } else {
                                navController.navigate("tecnicoTicketDetail/${ticket.id_ticket}")
                            }
                        }
                    }
                }
            }
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
                        // En historial filtramos por categoría, estado siempre es "Cerrado"
                        ticketViewModel.getTickets(state = "Cerrado", technician = userId, category = categoryString)
                    } else {
                        // En pantalla normal filtramos por estado y categoría
                        ticketViewModel.getTickets(state = stateString, technician = userId, category = categoryString)
                    }
                },
                onClose = {
                    showFilterSheet = false
                }
            )
        }
    }
}
