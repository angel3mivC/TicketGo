package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ticketgo.data.models.GetUserResponse
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun AssignTechnicianModal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAssign: (Int) -> Unit,
    userViewModel: UserViewModel
) {
    val technicians by userViewModel.technicians.collectAsStateWithLifecycle()
    val isLoading by userViewModel.isLoading.collectAsStateWithLifecycle()
    
    var searchQuery by remember { mutableStateOf("") }
    var filteredTechnicians by remember { mutableStateOf<List<GetUserResponse>>(emptyList()) }
    
    // Filtrar técnicos basado en la búsqueda
    LaunchedEffect(technicians, searchQuery) {
        filteredTechnicians = if (searchQuery.isBlank()) {
            technicians
        } else {
            technicians.filter { 
                it.nombre.contains(searchQuery, ignoreCase = true) 
            }
        }
    }
    
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Text(
                        text = "Técnicos Disponibles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.size(24.dp)) // Espacio para centrar el título
                }
            },
            text = {
                Column {
                    // Barra de búsqueda
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Buscar",
                                color = Color.Gray
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFFD32F2F)
                            )
                        }
                    } else {
                        // Lista de técnicos filtrados
                        Column(
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            filteredTechnicians.forEachIndexed { index, technician ->
                                TechnicianItem(
                                    technician = technician,
                                    onAssign = { onAssign(technician.id_usuario) }
                                )
                                
                                // Agregar línea divisoria entre técnicos (excepto el último)
                                if (index < filteredTechnicians.size - 1) {
                                    androidx.compose.material3.Divider(
                                        color = Color(0xFFBDBDBD),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                            
                            if (filteredTechnicians.isEmpty()) {
                                Text(
                                    text = if (searchQuery.isBlank()) "No hay técnicos disponibles" else "No se encontraron técnicos",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun TechnicianItem(
    technician: GetUserResponse,
    onAssign: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onAssign() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = technician.nombre.take(1).uppercase(),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Column {
                Text(
                    text = technician.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                // Estado del técnico
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (technician.estado == "Activo") Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                            )
                    )
                    
                    Text(
                        text = technician.estado,
                        fontSize = 12.sp,
                        color = if (technician.estado == "Activo") Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                    )
                }
            }
        }
        
        // Botón de asignar
        IconButton(
            onClick = onAssign,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFD32F2F))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Asignar",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
