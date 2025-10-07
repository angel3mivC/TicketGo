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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import mx.tec.ticketgo.ui.models.Ticket
import mx.tec.ticketgo.data.repository.TicketRepository
import mx.tec.ticketgo.ui.components.BodyText
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.TicketAdminPreview
import mx.tec.ticketgo.ui.components.Title


@Composable
fun AdminTicketScreen() {
    val context = LocalContext.current
    val repository = remember { TicketRepository(context) }

    var tickets by remember { mutableStateOf<List<Ticket>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null

        repository.getTickets { jsonArray ->
            isLoading = false

            if (jsonArray != null) {
                tickets = repository.parseTickets(jsonArray)
            } else {
                errorMessage = "Error al cargar los tickets"
            }
        }
    }

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
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Error",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

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

val mockTickets = listOf(
    Ticket(
        ticketId = 1,
        title = "Error en inicio de sesión",
        description = "El usuario no puede iniciar sesión en la plataforma, muestra un error 401.",
        categoryId = 2,
        priorityId = 1,
        status = "Abierto",
        tecnico = "Carlos López",
        startDate = "2025-10-05 10:23:00"
    ),
    Ticket(
        ticketId = 2,
        title = "Pantalla en blanco en módulo de reportes",
        description = "Al acceder a la sección de reportes, la pantalla queda en blanco sin mostrar datos.",
        categoryId = 3,
        priorityId = 2,
        status = "En progreso",
        tecnico = "María González",
        startDate = "2025-10-04 14:10:00"
    ),
    Ticket(
        ticketId = 3,
        title = "Fallo en conexión a base de datos",
        description = "El sistema no logra conectarse a la base de datos en producción.",
        categoryId = 1,
        priorityId = 3,
        status = "Cerrado",
        tecnico = "Luis Fernández",
        startDate = "2025-10-01 08:45:00"
    ),
    Ticket(
        ticketId = 4,
        title = "Error al generar facturas PDF",
        description = "Las facturas se descargan con campos vacíos en el documento.",
        categoryId = 4,
        priorityId = 2,
        status = "Abierto",
        tecnico = "Ana Torres",
        startDate = "2025-09-30 16:50:00"
    ),
    Ticket(
        ticketId = 5,
        title = "Problemas con notificaciones push",
        description = "Las notificaciones no se envían correctamente a los dispositivos Android.",
        categoryId = 2,
        priorityId = 1,
        status = "En progreso",
        tecnico = "Jorge Ramírez",
        startDate = "2025-09-28 11:15:00"
    )
)

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
