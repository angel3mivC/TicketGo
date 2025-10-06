package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ticketgo.ui.theme.ChipsBlue
import mx.tec.ticketgo.ui.theme.ChipsGray
import mx.tec.ticketgo.ui.theme.ChipsGreen
import mx.tec.ticketgo.ui.theme.ChipsRed
import mx.tec.ticketgo.ui.theme.ChipsYellow

@Composable
fun TicketButtomChips(prioridad: Int, categoria: Int) {
    val valorPrioridad = when(prioridad) {
        1 -> "Baja"
        2 -> "Media"
        3 -> "Alta"
        else -> "Prioridad indefinida"
    }
    val colorPrioridad = when(valorPrioridad) {
        "Baja" -> MaterialTheme.colorScheme.ChipsGreen
        "Media" -> MaterialTheme.colorScheme.ChipsYellow
        "Alta" -> MaterialTheme.colorScheme.ChipsRed
        else -> MaterialTheme.colorScheme.outline
    }

    val valorCategoria = when(categoria) {
        1 -> "Categoría 1"
        2 -> "Categoría 2"
        3 -> "Categoría 3"
        4 -> "Categoría 4"
        else -> "Categoría indefinida"
    }
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Chip(
            text = valorCategoria,
            color = Color.DarkGray,
            modifier = Modifier.padding(end = 8.dp)
        )
        DotChip(valorPrioridad, colorPrioridad)
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

// Modelo de comentario
data class Comment(
    val id: String,
    val userName: String,
    val content: String,
    val timestamp: String
)

@Composable
fun TicketCommentsSection(
    comments: List<Comment>,
    onSendComment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Lista de comentarios
        if (comments.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                comments.forEach { comment ->
                    CommentItem(comment = comment)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Campo de entrada
        CommentInputField(
            value = commentText,
            onValueChange = { commentText = it },
            onSend = {
                if (commentText.isNotBlank()) {
                    onSendComment(commentText)
                    commentText = ""
                }
            }
        )
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        InitialsAvatar(
            nombre = comment.userName,
            size = 40.dp,
            backgroundColor = getColorForName(comment.userName)
        )

        // Contenido del comentario
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Nombre y fecha juntos
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.userName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = comment.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Texto del comentario
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF424242)
            )
        }
    }
}

@Composable
fun CommentInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Ingresa un comentario",
                color = Color(0xFFBDBDBD)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (value.isNotBlank()) Color.Black else Color(0xFFE0E0E0),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar comentario",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFE0E0E0),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        maxLines = 3
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTicketCommentsSection() {
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
        ),
        Comment(
            id = "3",
            userName = "Carlos Ramírez",
            content = "Ya está casi terminado, solo falta realizar las pruebas finales.",
            timestamp = "3 sept 2023 · 9:15 am"
        ),
        Comment(
            id = "4",
            userName = "Ana Martínez",
            content = "Perfecto, gracias por el avance.",
            timestamp = "3 sept 2023 · 11:45 am"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        TicketCommentsSection(
            comments = mockComments,
            onSendComment = { }
        )
    }
}