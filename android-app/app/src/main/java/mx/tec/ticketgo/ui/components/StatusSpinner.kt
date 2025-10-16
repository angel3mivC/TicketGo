package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Enum para definir los tipos de usuario y sus estados permitidos
enum class UserType {
    ADMIN,
    TECNICO
}

// Estados disponibles del ticket
enum class TicketStatus(val displayName: String, val color: Color, val id: Int) {
    ABIERTO("Abierto", Color(0xFF4CAF50), 1),
    EN_PROGRESO("En Progreso", Color(0xFFFF9800), 2),
    RESUELTO("Resuelto", Color(0xFF2196F3), 4),
    CERRADO("Cerrado", Color(0xFFF44336), 3),
    REABIERTO("Reabierto", Color(0xFF9E9E9E), 5)
}

@Composable
fun StatusSpinner(
    currentStatus: String,
    userType: UserType,
    hasAssignedTechnician: Boolean = true,
    onStatusChange: (String, Int) -> Unit,
    onError: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Obtener estados permitidos según el tipo de usuario
    val allowedStatuses = when (userType) {
        UserType.ADMIN -> listOf(
            TicketStatus.ABIERTO,
            TicketStatus.EN_PROGRESO,
            TicketStatus.RESUELTO,
            TicketStatus.CERRADO
        )
        UserType.TECNICO -> listOf(
            TicketStatus.ABIERTO,
            TicketStatus.EN_PROGRESO,
            TicketStatus.RESUELTO
        )
    }
    
    // Encontrar el estado actual
    val currentTicketStatus = TicketStatus.values().find { 
        it.displayName == currentStatus 
    } ?: TicketStatus.ABIERTO
    
    Box(modifier = modifier) {
        // Chip simple sin bordes ni flecha
        DotChip(
            text = currentTicketStatus.displayName,
            color = currentTicketStatus.color,
            modifier = Modifier.clickable { 
                if (hasAssignedTechnician) {
                    expanded = true
                } else {
                    onError("No se puede modificar el estado de un ticket que aún no tiene técnico asignado")
                }
            }
        )
        
        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            allowedStatuses.forEach { status ->
                DropdownMenuItem(
                    text = {
                        // Usar DotChip existente para cada opción
                        DotChip(
                            text = status.displayName,
                            color = status.color,
                            modifier = Modifier.wrapContentWidth()
                        )
                    },
                    onClick = {
                        onStatusChange(status.displayName, status.id)
                        expanded = false
                    },
                    modifier = Modifier.background(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun StatusSpinnerPreview() {
    var currentStatus by remember { mutableStateOf("Abierto") }
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Admin Spinner:")
        StatusSpinner(
            currentStatus = currentStatus,
            userType = UserType.ADMIN,
            hasAssignedTechnician = true,
            onStatusChange = { status, _ -> currentStatus = status },
            onError = { error -> println("Error: $error") }
        )
        
        Text("Técnico Spinner:")
        StatusSpinner(
            currentStatus = currentStatus,
            userType = UserType.TECNICO,
            hasAssignedTechnician = true,
            onStatusChange = { status, _ -> currentStatus = status },
            onError = { error -> println("Error: $error") }
        )
        
        Text("Sin técnico asignado:")
        StatusSpinner(
            currentStatus = currentStatus,
            userType = UserType.ADMIN,
            hasAssignedTechnician = false,
            onStatusChange = { status, _ -> currentStatus = status },
            onError = { error -> println("Error: $error") }
        )
    }
}
