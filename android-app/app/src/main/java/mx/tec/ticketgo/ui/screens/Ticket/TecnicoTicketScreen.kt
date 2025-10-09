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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.data.repository.TicketRepository
import mx.tec.ticketgo.ui.components.Chip
import mx.tec.ticketgo.ui.components.TicketTecnicoPreview
import mx.tec.ticketgo.ui.components.Title
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel


@Composable
fun TecnicoTicketScreen(viewModel: TicketsViewModel, id: Int) {


    // 1. Obtener contexto y crear repository
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("id_usuario", -1)


    // 2. Estados
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    // Acceder a un atributo de la lista
    // tickets[0].id_ticket
    // Esta linea llama al metodo
    if (userId != -1)viewModel.getTickets(technician = userId)





    // 4. UI
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

        // Contenido según estado
        when {
            /*
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
                    val message: String = errorMessage ?: "Error desconocido"
                    Text(
                        text = message,
                        color = Color.Red
                    )
                }
            }
*/
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = tickets.size
                    ) { index ->
                        TicketTecnicoPreview(ticket = tickets[index])
                    }
                }
            }
        }
    }
}



