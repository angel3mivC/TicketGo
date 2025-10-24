package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
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
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.ui.theme.ChipsBlue
import mx.tec.ticketgo.ui.theme.ChipsGray
import mx.tec.ticketgo.ui.theme.ChipsGreen
import mx.tec.ticketgo.ui.theme.ChipsRed
import mx.tec.ticketgo.ui.theme.ChipsYellow
import mx.tec.ticketgo.utils.DateFormatter

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
            color = Color.DarkGray, // SE DEBE USAR UN COLOR DE COLOR THEME
            modifier = Modifier.padding(end = 8.dp)
        )
        DotChip(prioridad, colorPrioridad)
    }
}

@Composable
fun TicketButtomChipsClickable(
    prioridad: String, 
    categoria: String,
    onPriorityClick: () -> Unit,
    onCategoryClick: () -> Unit,
    isPriorityClickable: Boolean = true,
    isCategoryClickable: Boolean = true
) {
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
            modifier = Modifier
                .padding(end = 8.dp)
                .then(
                    if (isCategoryClickable) {
                        Modifier.clickable { onCategoryClick() }
                    } else {
                        Modifier
                    }
                )
        )
        DotChip(
            text = prioridad,
            color = colorPrioridad,
            modifier = Modifier.then(
                if (isPriorityClickable) {
                    Modifier.clickable { onPriorityClick() }
                } else {
                    Modifier
                }
            )
        )
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
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false) // 🔥 IMPORTANTE: fill = false para no expandirse
                .padding(end = 8.dp) // 🔥 Espacio entre título y chip
        ) {
            Subtitle(titulo)
            SmallText(DateFormatter.formatDate(fechaHora))
        }

        // Chip se adapta al contenido pero no se aplasta
        DotChip(
            text = estado,
            color = colorEstado,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
fun TicketTopWithSpinner(
    titulo: String, 
    fechaHora: String, 
    estado: String,
    userType: UserType,
    hasAssignedTechnician: Boolean = true,
    isHistory: Boolean = false,
    onStatusChange: (String, Int) -> Unit,
    onError: (String) -> Unit = {}
) {
    // Estado para mensajes de error
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false) // 🔥 IMPORTANTE: fill = false para no expandirse
                    .padding(end = 8.dp) // 🔥 Espacio entre título y spinner
            ) {
                Subtitle(titulo)
                SmallText(DateFormatter.formatDate(fechaHora))
            }

            // Spinner para cambiar estado
            StatusSpinner(
                currentStatus = estado,
                userType = userType,
                hasAssignedTechnician = hasAssignedTechnician,
                isHistory = isHistory,
                onStatusChange = onStatusChange,
                onError = { message -> errorMessage = message },
                modifier = Modifier.wrapContentWidth()
            )
        }
        
        // Fila separada para mensajes de error
        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFFD32F2F),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
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
fun GalleryButton(
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
                imageVector = Icons.Default.List,
                contentDescription = "Galería",
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

@Composable
fun EvidenciasTicketTecnico(
    onAddEvidence: () -> Unit,
    onViewGallery: () -> Unit
) {
    Column {
        Subtitle("Evidencias")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AddButton(
                "Agregar",
                onAddEvidence
            )
            GalleryButton(
                "Galería",
                onViewGallery
            )
        }
    }
}

@Composable
fun EvidenciasTicketAdmin(
    onViewGallery: () -> Unit
) {
    Column {
        Subtitle("Evidencias")
        GalleryButton(
            "Galería",
            onViewGallery
        )
    }
}


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
    val nombre = if (comment.autor != null) comment.autor else "Desconocido"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Avatar
        InitialsAvatar(
            nombre = nombre,
            size = 40.dp,
            backgroundColor = getColorForName(nombre)
        )

        // Contenido del comentario
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Solo el nombre del usuario
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Texto del comentario y fecha en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.comentario,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242),
                    modifier = Modifier.weight(1f)
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
                ) {
                    val (date, time) = DateFormatter.formatNotificationDate(comment.fecha)
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp
                    )
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9E9E9E),
                        fontSize = 12.sp
                    )
                }
            }
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

@Composable
fun EvidenciasTicketTecnicoHistorial(
    onViewGallery: () -> Unit
) {
    Column {
        Subtitle("Evidencias")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Solo botón de galería, sin botón de agregar
            GalleryButton(
                "Galería",
                onViewGallery
            )
        }
    }
}

@Composable
fun TicketCommentsSectionHistorial(
    comments: List<Comment>
) {
    Column {
        Subtitle("Comentarios")
        
        if (comments.isEmpty()) {
            Text(
                text = "No hay comentarios",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            comments.forEach { comment ->
                Column(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    CommentItem(comment = comment)
                }
            }
        }
    }
}
