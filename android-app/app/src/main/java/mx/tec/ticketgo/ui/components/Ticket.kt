package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.models.Ticket

@Composable
fun TicketTecnico(
    ticket: Ticket,
    comments: List<Comment>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        TicketTop(
            titulo = ticket.title,
            fechaHora = ticket.startDate ?: "Sin fecha",
            estado = ticket.status
        )

        TicketButtomChips(
            prioridad = ticket.priorityId,
            categoria = ticket.categoryId
        )

        Spacer(modifier = Modifier.height(32.dp))

        BodyText(ticket.description)

        Spacer(modifier = Modifier.height(16.dp))

        EvidenciasTicket({})

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // Sección de comentarios
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
        title = "Titulo",
        description = "Descripción.LoremIpsumLorem Ipsum. LoremLoremLorem LoremLorem",
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