package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.Ticket


@Composable
fun TicketTecnico(
    ticket: Ticket,
    comments: List<Comment>,
    onSendComment: (String) -> Unit,
    onAddEvidence: () -> Unit = {},
    onViewGallery: () -> Unit = {},
    onStatusChange: (String, Int) -> Unit = { _, _ -> }
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
        TicketTopWithSpinner(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion ?: "Sin fecha",
            estado = ticket.estado!!,
            userType = UserType.TECNICO,
            hasAssignedTechnician = ticket.asignado_a != null,
            onStatusChange = onStatusChange,
            onError = { /* Manejar error si es necesario */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Evidencias
        EvidenciasTicketTecnico(
            onAddEvidence = onAddEvidence,
            onViewGallery = onViewGallery
        )

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // 🔹 Sección de comentarios
        TicketCommentsSection(
            comments = comments,
            onSendComment = onSendComment
        )
    }
}

@Composable
fun TicketAdmin(
    ticket: Ticket,
    comments: List<Comment>,
    onSendComment: (String) -> Unit,
    onViewGallery: () -> Unit = {},
    onStatusChange: (String, Int) -> Unit = { _, _ -> },
    onAssignTechnician: () -> Unit = {}
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
        TicketTopWithSpinner(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion ?: "Sin fecha",
            estado = ticket.estado!!,
            userType = UserType.ADMIN,
            hasAssignedTechnician = ticket.asignado_a != null,
            onStatusChange = onStatusChange,
            onError = { /* Manejar error si es necesario */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Avatar del técnico asignado y nombre
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Row interna para avatar y nombre (mantener juntos)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ticket.asignado_a != null) {
                    // Técnico asignado - mostrar InitialsAvatar
                    InitialsAvatar(
                        nombre = ticket.asignado_a!!,
                        backgroundColor = getColorForName(ticket.asignado_a!!)
                    )
                    BodyText(ticket.asignado_a!!)
                } else {
                    // Sin técnico asignado - mostrar círculo con ícono de más clickeable
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFE0E0E0),
                                shape = CircleShape
                            )
                            .clickable { onAssignTechnician() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Asignar técnico",
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    BodyText("Sin asignar")
                }
            }
            
            // Botón para reasignar técnico (solo si hay técnico asignado)
            if (ticket.asignado_a != null) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .clickable { onAssignTechnician() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Reasignar técnico",
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Evidencias
        EvidenciasTicketAdmin(
            onViewGallery = onViewGallery
        )

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // 🔹 Sección de comentarios
        TicketCommentsSection(
            comments = comments,
            onSendComment = onSendComment
        )
    }
}

@Composable
fun TicketTecnicoHistorial(
    ticket: Ticket,
    comments: List<Comment>,
    onViewGallery: () -> Unit = {}
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
        // 🔹 Cabecera del ticket (título, fecha y estado) - SIN SPINNER
        TicketTop(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion ?: "Sin fecha",
            estado = ticket.estado!!
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Evidencias - SOLO VER, NO SUBIR
        EvidenciasTicketTecnicoHistorial(
            onViewGallery = onViewGallery
        )

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // 🔹 Sección de comentarios - SOLO LECTURA
        TicketCommentsSectionHistorial(
            comments = comments
        )
    }
}

@Composable
fun TicketAdminHistorial(
    ticket: Ticket,
    comments: List<Comment>,
    onViewGallery: () -> Unit = {},
    onAssignTechnician: () -> Unit = {}
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
        // 🔹 Cabecera del ticket (título, fecha y estado) - SIN SPINNER
        TicketTop(
            titulo = ticket.titulo,
            fechaHora = ticket.fecha_creacion ?: "Sin fecha",
            estado = ticket.estado!!
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Chips de prioridad y categoría
        TicketButtomChips(
            prioridad = ticket.prioridad!!,
            categoria = ticket.categoria!!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Avatar del técnico asignado y nombre
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Row interna para avatar y nombre (mantener juntos)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ticket.asignado_a != null) {
                    // Técnico asignado - mostrar InitialsAvatar
                    InitialsAvatar(
                        nombre = ticket.asignado_a!!,
                        backgroundColor = getColorForName(ticket.asignado_a!!)
                    )
                    BodyText(ticket.asignado_a!!)
                } else {
                    // Sin técnico asignado - mostrar círculo con ícono de más clickeable
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFE0E0E0),
                                shape = CircleShape
                            )
                            .clickable { onAssignTechnician() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Asignar técnico",
                            tint = Color(0xFF9E9E9E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    BodyText("Sin asignar")
                }
            }
            
            // Botón para reasignar técnico (solo si hay técnico asignado)
            if (ticket.asignado_a != null) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .clickable { onAssignTechnician() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Reasignar técnico",
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Descripción del ticket
        BodyText(ticket.descripcion)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Evidencias - SOLO VER
        EvidenciasTicketAdmin(
            onViewGallery = onViewGallery
        )

        Spacer(modifier = Modifier.height(16.dp))

        LineaPunteada()

        // 🔹 Sección de comentarios - SOLO LECTURA
        TicketCommentsSectionHistorial(
            comments = comments
        )
    }
}