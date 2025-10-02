package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.ChipsBlue
import mx.tec.ticketgo.ui.theme.ChipsGray
import mx.tec.ticketgo.ui.theme.ChipsGreen
import mx.tec.ticketgo.ui.theme.ChipsRed
import mx.tec.ticketgo.ui.theme.ChipsYellow

@Composable
fun TicketButtomChips(prioridad: String, categoria: String) {
    val colorPrioridad = when(prioridad) {
        "Baja" -> MaterialTheme.colorScheme.ChipsGreen
        "Media" -> MaterialTheme.colorScheme.ChipsYellow
        "Alta" -> MaterialTheme.colorScheme.ChipsRed
        else -> MaterialTheme.colorScheme.outline
    }
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Chip(
            text = categoria,
            color = Color.DarkGray,
            modifier = Modifier.padding(end = 8.dp)
        )
        DotChip(prioridad, colorPrioridad)
    }
}

@Composable
fun TicketTop(titulo: String, fechaHora: String, estado: String) {
    val colorEstado = when (estado) {
        "Abierto" -> MaterialTheme.colorScheme.ChipsGreen
        "En Progreso" -> MaterialTheme.colorScheme.ChipsYellow
        "Resuelto" -> MaterialTheme.colorScheme.ChipsBlue
        "Cerrado" -> MaterialTheme.colorScheme.ChipsRed
        "Reabierto" -> MaterialTheme.colorScheme.ChipsGray
        else -> MaterialTheme.colorScheme.outline
    }
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
        DotChip(estado,colorEstado)
    }
}

@Composable
fun LineaPunteada() {
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
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun AddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.size(width = 100.dp, height = 80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun EvidenciasTicket(onClickEvidencias: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Subtitle("Evidencias")
            TextButton({}) {
                SmallText("Ver Todos >")
            }
        }
        AddButton(
            "Agregar",
            onClickEvidencias
        )

    }
}


