package mx.tec.ticketgo.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.data.models.Ticket


@Composable
fun TicketTecnicoPreview(ticket: Ticket, onClick: (Ticket) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable { onClick(ticket) } // Detectar click para mostrar detalles
    ) {
        TicketTop(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion,
            estado = ticket.estado!!
        )

        BodyText(
            text = ticket.descripcion,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(16.dp))
        LineaPunteada()
    }
}


@Composable
fun TicketAdminPreview(ticket: Ticket, onClick: (Ticket) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White) // SE DEBE USAR COLOR THEME
            .padding(16.dp)
            .clickable{onClick(ticket)} //Accion al hacer click
    ) {

        TicketTop(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion,
            estado = ticket.estado!!
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                text = ticket.descripcion,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(2f)
            )

            ticket.asignado_a?.let { nombre ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    InitialsAvatar(
                        nombre = nombre,
                        backgroundColor = getColorForName(nombre)
                    )
                    SmallText(nombre)
                }
            }
        }

        TicketButtomChips(
            prioridad = ticket.prioridad!!,  // Temporal hasta que arregles el backend
            categoria = ticket.categoria!!   // Temporal hasta que arregles el backend
        )
        LineaPunteada()
    }
}

