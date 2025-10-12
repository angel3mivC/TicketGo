package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.Ticket


@Composable
fun TicketTecnico(
    ticket: Ticket,
    comments: List<Comment>
) {
    Column(
        modifier = Modifier
            .wrapContentHeight() // ✅ evita que se alargue feo
            .fillMaxWidth(0.95f) // ✅ que no ocupe todo el ancho
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 🔹 Cabecera del ticket (título, fecha y estado)
        TicketTop(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion ?: "Sin fecha",
            estado = ticket.estado!!
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Evidencias
        EvidenciasTicket({})

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // 🔹 Sección de comentarios
        TicketCommentsSection(
            comments = comments,
            onSendComment = { /* Manejar envío de comentario */ }
        )
    }
}
