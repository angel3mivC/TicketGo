package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TicketTecnico(titulo: String, fechaHora: String, estado: String, categoria: String, prioridad: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        TicketTop(titulo, fechaHora, estado)
        TicketButtomChips(prioridad, categoria)
        Spacer(modifier = Modifier.height(32.dp))
        BodyText("Descripción.LoremIpsumLorem Ipsum. LoremLoremLorem LoremLorem")
        Spacer(modifier = Modifier.height(16.dp))
        EvidenciasTicket({})
        LineaPunteada()

    }
}

@Preview
@Composable
fun TicketPreview() {
    TicketTecnico("Titulo", "2 sept 2023", "Abierto", "Garantía", "Alta")
}