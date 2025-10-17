package mx.tec.ticketgo.ui.screens.notifcations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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