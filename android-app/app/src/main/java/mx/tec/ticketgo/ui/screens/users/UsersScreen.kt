package mx.tec.ticketgo.ui.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.data.models.GetUserResponse
import mx.tec.ticketgo.ui.components.EmptyState
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun UsersScreen(
    userViewModel: UserViewModel,
    navController: NavController
) {
    val users by userViewModel.users.collectAsStateWithLifecycle()
    val isLoading by userViewModel.isLoading.collectAsStateWithLifecycle()
    val message by userViewModel.message.collectAsStateWithLifecycle()
    
    // Estado para el diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<GetUserResponse?>(null) }

    // Recargar usuarios cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        userViewModel.getUsers()
    }

    // Mostrar mensaje de éxito/error
    LaunchedEffect(message) {
        if (message != null) {
            // Recargar la lista de usuarios después de eliminar
            userViewModel.getUsers()
            userViewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            UsersTopBar(
                title = "Mis usuarios",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Mostrar mensaje de éxito/error
            message?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (msg.contains("éxito") || msg.contains("eliminado")) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Lista de usuarios
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                users.isEmpty() -> {
                    EmptyState(
                        imageRes = mx.tec.ticketgo.R.drawable.sin_usuarios,
                        title = "Sin usuarios",
                        subtitle = "Empieza a crear nuevos\nusuarios y aquí aparecerán."
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(users) { user ->
                            UserItem(
                                user = user,
                                onEdit = { 
                                    navController.navigate("editUserForm/${user.id_usuario}")
                                },
                                onDelete = { 
                                    // Mostrar diálogo de confirmación
                                    userToDelete = user
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Diálogo de confirmación para eliminar usuario
    if (showDeleteDialog && userToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                userToDelete = null
            },
            title = {
                Text(
                    text = "Confirmar eliminación",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas deshabilitar al usuario ${userToDelete?.nombre}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        userToDelete?.let { user ->
                            userViewModel.disableUser(
                                id = user.id_usuario,
                                name = user.nombre,
                                email = user.correo,
                                password = "", // No necesitamos la contraseña para deshabilitar
                                roleId = user.id_rol
                            )
                        }
                        showDeleteDialog = false
                        userToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        userToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun UserItem(
    user: GetUserResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circular
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.nombre?.take(1)?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Información del usuario
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.nombre ?: "Sin nombre",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = getRoleName(user.id_rol),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Botones de acción
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                onClick = onEdit
            ) {
                Text("Editar")
            }
            
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar usuario",
                    tint = Color.Red
                )
            }
        }
    }
}

private fun getRoleName(roleId: Int?): String {
    return when (roleId) {
        1 -> "Admin"
        2 -> "Mesa"
        3 -> "Técnico"
        else -> "Sin rol"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersTopBar(
    title: String,
    navController: NavController
) {
    TopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { 
                    navController.navigate("adminHome") {
                        popUpTo("adminHome") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        },
        actions = {
            Button(
                onClick = { navController.navigate("createUserForm") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Crear usuario",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}
