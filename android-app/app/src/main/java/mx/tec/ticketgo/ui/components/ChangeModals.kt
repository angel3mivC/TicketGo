package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun PriorityChangeModal(
    isVisible: Boolean,
    currentPriority: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var selectedPriority by remember { mutableStateOf(currentPriority) }
    var selectedId by remember { mutableStateOf(getPriorityId(currentPriority)) }
    
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título
                    Text(
                        text = "Cambiar Prioridad",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Opciones de prioridad usando Column para evitar aplastamiento
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val priorities = listOf(
                            Triple("Alta", Color(0xFFE57373), 1),
                            Triple("Media", Color(0xFFFFB74D), 2),
                            Triple("Baja", Color(0xFF81C784), 3)
                        )
                        
                        priorities.forEach { (name, color, id) ->
                            PriorityOption(
                                name = name,
                                color = color,
                                isSelected = selectedPriority == name,
                                onClick = { 
                                    selectedPriority = name
                                    selectedId = id
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Botones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Button(
                            onClick = { 
                                onConfirm(selectedPriority, selectedId)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Confirmar",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getPriorityId(priority: String): Int {
    return when(priority) {
        "Alta" -> 1
        "Media" -> 2
        "Baja" -> 3
        else -> 2
    }
}

@Composable
fun CategoryChangeModal(
    isVisible: Boolean,
    currentCategory: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(currentCategory) }
    var selectedId by remember { mutableStateOf(getCategoryId(currentCategory)) }
    
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título
                    Text(
                        text = "Cambiar Categoría",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Opciones de categoría usando Column para evitar aplastamiento
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val categories = listOf(
                            Pair("En proceso", 1),
                            Pair("Daño inducido", 2),
                            Pair("Garantía", 3)
                        )
                        
                        categories.forEach { (name, id) ->
                            CategoryOption(
                                name = name,
                                isSelected = selectedCategory == name,
                                onClick = { 
                                    selectedCategory = name
                                    selectedId = id
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Botones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Button(
                            onClick = { 
                                onConfirm(selectedCategory, selectedId)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Confirmar",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getCategoryId(category: String): Int {
    return when(category) {
        "En proceso" -> 1
        "Daño inducido" -> 2
        "Garantía" -> 3
        else -> 1
    }
}

@Composable
fun PriorityOption(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) color.copy(alpha = 0.1f) else Color(0xFFF5F5F5)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Usar DotChip existente
            DotChip(
                text = name,
                color = color
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Indicador de selección
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun CategoryOption(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Color(0xFFE0E0E0) else Color(0xFFF5F5F5)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Usar Chip existente
            Chip(
                text = name,
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Indicador de selección
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
    }
}
