package mx.tec.ticketgo.ui.screens.Notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.components.NotificationItem
import mx.tec.ticketgo.ui.components.NotificationRow

@Composable
fun NotificationsScreen(notifications: List<NotificationItem>) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(20.dp)
    ) {
        // Lista de notificaciones
        Column {
            notifications.forEach { notification ->
                NotificationRow(notification)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
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
        ),
        NotificationItem(
            initials = "LP",
            name = "Luis Pérez",
            role = "Técnico",
            description = "La revisión del ticket fue completada",
            timeAgo = "Hace 3h"
    )
    )
    NotificationsScreen(notifications = sampleNotifications)
}