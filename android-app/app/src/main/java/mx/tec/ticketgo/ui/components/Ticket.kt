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
import mx.tec.ticketgo.ui.models.Comment
import mx.tec.ticketgo.ui.models.Ticket

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
            titulo = ticket.title,
            fechaHora = ticket.startDate ?: "Sin fecha",
            estado = ticket.status
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.priorityId,
            categoria = ticket.categoryId
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.description)

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

@Preview
@Composable
fun TicketPreview() {
    val mockTicket = Ticket(
        ticketId = 1,
        title = "Problema con el login de usuarios",
        description = "El sistema no permite el acceso y muestra error 401. Revisar autenticación.",
        categoryId = 2,
        priorityId = 1,
        status = "Abierto",
        tecnico = null,
        startDate = "2 sept 2023"
    )

    val mockComments = listOf(
        Comment(
            id = "1",
            userName = "Juan Pérez",
            content = "He revisado el ticket y ya estoy trabajando en la solución.",
            timestamp = "2 sept 2023 · 10:00 am"
        ),
        Comment(
            id = "2",
            userName = "María González",
            content = "¿Necesitas algún archivo adicional?",
            timestamp = "2 sept 2023 · 2:30 pm"
        )
    )

    TicketTecnico(
        ticket = mockTicket,
        comments = mockComments
    )
}
