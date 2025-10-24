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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.theme.Primary
import mx.tec.ticketgo.ui.viewmodels.LoginViewModel

@Composable
fun TopBarItem(
    icon: ImageVector,
    contentDescription: String,
    notificationCount: Int = 0,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Notificaciones",
            modifier = Modifier.size(38.dp)
        )

        if (notificationCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = -5.dp, y = 5.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (notificationCount > 99) "99" else notificationCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar(
    name: String,
    role: String,
    notificationCount: Int,
    navController: NavController,
) {
    var showUserMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()
    
    TopAppBar(
        title = {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = role,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        },
        navigationIcon = {
            Box {
                IconButton(
                    onClick = { showUserMenu = true }
                ) {
                    InitialsAvatar(
                        nombre = name,
                        size = 50.dp,
                        backgroundColor = getColorForName(name)
                    )
                }
                
                // Mini menú desplegable
                DropdownMenu(
                    expanded = showUserMenu,
                    onDismissRequest = { showUserMenu = false },
                    modifier = Modifier
                        .width(280.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                ) {
                    // Información del usuario (solo visual, no clickeable)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InitialsAvatar(
                                nombre = name,
                                size = 40.dp,
                                backgroundColor = getColorForName(name)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = role,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    
                    // Botón de cerrar sesión centrado
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Cerrar sesión",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Cerrar sesión",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            showUserMenu = false
                            // Implementar funcionalidad de cerrar sesión
                            loginViewModel.logout(context) {
                                // Navegar al login después del logout exitoso
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        },
        actions = {
            TopBarItem(
                icon = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                notificationCount = notificationCount,
                onClick = {
                    navController.navigate("notificaciones") {
                        launchSingleTop = true
                    }
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}