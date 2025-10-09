package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ticketgo.ui.theme.CircleBackground
import mx.tec.ticketgo.ui.theme.Primary

data class NotificationItem(
    val initials: String,
    val name: String,
    val role: String,
    val description: String,
    val timeAgo: String,
    val unread: Boolean = false
)

@Composable
fun NotificationRow(notification: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CircleBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = notification.initials,
                color = Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Contenedor principal: nombre, rol y descripción
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = notification.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Text(
                text = notification.role,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.description,
                fontSize = 14.sp,
            )
        }

        // Contenedor derecho: tiempo notificacion y punto rojo
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                if (notification.unread) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = notification.timeAgo,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationRowPreview() {
    val sampleNotification = NotificationItem(
        initials = "AG",
        name = "Alberto Gómez",
        role = "Técnico",
        description = "Tienes un nuevo ticket asignado",
        timeAgo = "Hace 1h",
        unread = true
    )
    NotificationRow(notification = sampleNotification)
}
