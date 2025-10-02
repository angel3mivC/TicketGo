package mx.tec.ticketgo.ui.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.ChipsBlue
import mx.tec.ticketgo.ui.theme.ChipsGray
import mx.tec.ticketgo.ui.theme.ChipsGreen
import mx.tec.ticketgo.ui.theme.ChipsRed
import mx.tec.ticketgo.ui.theme.ChipsYellow


// Versión alternativa más simple si quieres solo la línea punteada inferior
@Composable
fun TicketTecnicoPreview(titulo: String, fechaHora: String, descripcion: String, categoria: String, estado: String, prioridad: String) {

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

        BodyText(
            text = descripcion,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TicketButtomChips(prioridad, categoria)
        Spacer(modifier = Modifier.height(16.dp))
        LineaPunteada()

    }
}
@Composable
fun TicketAdminPreview(titulo: String, fechaHora: String, descripcion: String, categoria: String, estado: String, prioridad: String, nombre: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        TicketTop(titulo,fechaHora, estado)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                text = descripcion,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(2f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                InitialsAvatar(nombre = nombre, backgroundColor = getColorForName(nombre))
                SmallText(nombre)
            }
        }

        TicketButtomChips(prioridad, categoria)
        LineaPunteada()

    }
}


@Preview()
@Composable
fun PreviewTicket() {
    //TicketAdminPreview("Título", "2 sept 2023 8:35 am", "Descripción. Lorem Ipsum Lorem Impsum", "Garantía", "Abierto", "Media", "Osmar Sanchez")
    TicketTecnicoPreview("Título", "2 sept 2023 8:35 am", "Descripción. Lorem Ipsum Lorem Impsum", "Garantía", "Abierto", "Alta")
}