package mx.tec.ticketgo.ui.screens.notifcations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.tec.ticketgo.data.models.Notification
import mx.tec.ticketgo.ui.components.NotificationItem
import mx.tec.ticketgo.ui.components.NotificationRow
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.viewmodels.NotificationsViewModel

@Composable
fun NotificationsScreen(
    notificationsViewModel: NotificationsViewModel,
    navController: NavController
) {
    val notifications by notificationsViewModel.notifications.collectAsState()
    
    Scaffold(
        topBar = {
            TopBar(
                title = "Notificaciones",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            if (notifications.isEmpty()) {
                Text(
                    text = "No hay notificaciones",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(notifications, key = { it.id_notificacion }) { notification ->
                        NotificationRow(
                            notification = NotificationItem(
                                initials = "TG",
                                name = "TicketGo",
                                role = "Sistema",
                                description = notification.mensaje,
                                timeAgo = notification.fecha,
                                unread = notification.leido == 0 // 0 = no leído, 1 = leído
                            ),
                            onSwipeToRead = {
                                notificationsViewModel.markAsRead(notification.id_notificacion)
                            }
                        )
                    }
                }
            }
        }
    }
}