package mx.tec.ticketgo.ui.screens.MainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.NavBar
import mx.tec.ticketgo.ui.components.NotificationItem
import mx.tec.ticketgo.ui.components.UserTopBar
import mx.tec.ticketgo.ui.screens.History.HistoryScreen
import mx.tec.ticketgo.ui.screens.Home.HomeScreen
import mx.tec.ticketgo.ui.screens.Notifications.NotificationsScreen
import mx.tec.ticketgo.ui.screens.Ticket.TicketFormScreen

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            UserTopBar(
                initials = "AG",
                name = "Alejandra Galván",
                role = "Mesa de Trabajo",
                notificationCount = 3,
                navController = navController
            )
        },
        bottomBar = {
            NavBar(
                navController,
                Icons.Default.Add,
                "formulario"
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") { HomeScreen() }
            composable("formulario") { TicketFormScreen() }
            composable("historial") { HistoryScreen() }
            composable("notificaciones") {

                val sampleNotifications = listOf(
                    NotificationItem(
                        initials = "MB",
                        name = "MarGalván",
                        role = "Admin",
                        description = "Se añadió un nuevo usuario",
                        timeAgo = "Hace 1h",
                        unread = true
                    ),
                    NotificationItem(
                        initials = "AG",
                        name = "Alberto Gómez",
                        role = "Técnico",
                        description = "La revisión del ticket fue completada",
                        timeAgo = "hace 3 horas"
                    )
                )
                NotificationsScreen(notifications = sampleNotifications)
            }

        }
    }
}