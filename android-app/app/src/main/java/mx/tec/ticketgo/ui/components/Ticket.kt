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
        // Contenido
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Subtitle(titulo)
                SmallText(fechaHora)
            }
            DotChip(estado,MaterialTheme.colorScheme.primary)
        }

        BodyText(
            text = descripcion,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Chip(
                text = categoria,
                color = Color.Black,
                modifier = Modifier.padding(end = 8.dp)
            )
            DotChip(prioridad, Color.Red)
        }

        // Línea punteada inferior usando Canvas
        Spacer(modifier = Modifier.height(16.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        ) {
            val dashWidth = 6.dp.toPx()
            val dashGap = 4.dp.toPx()
            val totalWidth = size.width

            var xPos = 0f
            while (xPos < totalWidth) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(xPos, 0f),
                    end = Offset(xPos + dashWidth, 0f),
                    strokeWidth = 2.dp.toPx()
                )
                xPos += dashWidth + dashGap
            }
        }
    }
}
@Preview()
@Composable
fun PreviewTicket() {
    TicketTecnicoPreview("Título", "2 sept 2023 8:35 am", "Descripción. Lorem Ipsum Lorem Impsum", "Garantía", "Abierto", "Alta")
}