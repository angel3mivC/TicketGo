package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InitialsAvatar(
    nombre: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val initials = nombre
        .trim()
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")

    Box(
        modifier = modifier
            .size(size)
            .background(
                color = backgroundColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = textColor,
            fontSize = (size.value / 2.5).sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getColorForName(nombre: String): Color {
    val colors = listOf(
        Color(0xFFE57373), // Rojo
        Color(0xFF64B5F6), // Azul
        Color(0xFF81C784), // Verde
        Color(0xFFFFB74D), // Naranja
        Color(0xFF9575CD), // Morado
        Color(0xFF4DB6AC), // Turquesa
    )
    val hash = nombre.hashCode()
    return colors[Math.abs(hash) % colors.size]
}
