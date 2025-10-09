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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.models.Ticket
import mx.tec.ticketgo.data.repository.TicketRepository
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.TicketTecnico
import mx.tec.ticketgo.ui.components.TicketTecnicoPreview
import mx.tec.ticketgo.ui.components.Title
import mx.tec.ticketgo.ui.components.TopBar
import org.json.JSONArray
import org.json.JSONObject



@Composable
fun TecnicoTicketScreen(navController: NavController) {
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
        ),
        Ticket(
            ticketId = 6,
            title = "Falla al cargar dashboard",
            description = "El dashboard principal no carga los gráficos de desempeño.",
            categoryId = 3,
            priorityId = 2,
            status = "Pendiente",
            tecnico = "Mariana Pérez",
            startDate = "2025-10-02 09:20:00"
        ),
        Ticket(
            ticketId = 7,
            title = "Problemas con la impresora del área contable",
            description = "La impresora marca error de conexión con el servidor de impresión.",
            categoryId = 1,
            priorityId = 3,
            status = "Abierto",
            tecnico = "Eduardo Hernández",
            startDate = "2025-10-03 11:05:00"
        )
    )

    var selectedTicket by remember { mutableStateOf<Ticket?>(null) }
    var tickets by remember { mutableStateOf<List<Ticket>>(mockTickets) }

    // 💡 Si hay un ticket seleccionado, mostramos el detalle dentro de un Scaffold
    if (selectedTicket != null) {
        Scaffold(
            topBar = {
                TopBar("Detalles del ticket", navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                TicketTecnico(
                    ticket = selectedTicket!!,
                    comments = emptyList()
                )
            }
        }
    } else {
        // 💡 Si no hay ticket seleccionado, mostramos la lista de tickets
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Header con filtros
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

            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay tickets disponibles")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tickets.size) { index ->
                        TicketTecnicoPreview(
                            ticket = tickets[index],
                            onClick = { selectedTicket = it }
                        )
                    }
                }
            }
        }
    }
}


