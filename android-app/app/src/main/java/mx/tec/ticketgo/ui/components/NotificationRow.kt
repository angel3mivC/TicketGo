package mx.tec.ticketgo.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.tec.ticketgo.ui.theme.Primary
import mx.tec.ticketgo.utils.DateFormatter
import kotlin.math.roundToInt

data class NotificationItem(
    val initials: String,
    val name: String,
    val role: String,
    val description: String,
    val timeAgo: String,
    val unread: Boolean = false
)

@Composable
fun NotificationRow(
    notification: NotificationItem,
    onSwipeToRead: () -> Unit = {}
) {
    val (date, time) = DateFormatter.formatNotificationDate(notification.timeAgo)
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    
    // Auto-resetear si fue deslizado completamente
    LaunchedEffect(notification.unread) {
        if (!notification.unread) {
            offsetX.animateTo(0f, animationSpec = tween(300))
        }
    }
    
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    if (notification.unread) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    if (offsetX.value > 200f) {
                                        // Deslizamiento suficiente - marcar como leída
                                        offsetX.animateTo(1000f, animationSpec = tween(300))
                                        onSwipeToRead()
                                    } else {
                                        // Volver a la posición original
                                        offsetX.animateTo(0f, animationSpec = tween(300))
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                val newValue = (offsetX.value + dragAmount).coerceAtLeast(0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
            ) {
            val CircleBackground = if (notification.unread) Color(0xFFFFE0E0) else Color(0xFFF5F5F5)
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

            Spacer(modifier = Modifier.width(12.dp))

            // Contenedor principal: nombre, rol y descripción
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Primera fila: nombre y fecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )

                    // Solo la fecha
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = date,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = time,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = notification.role,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Fila de descripción con punto rojo centrado verticalmente
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.description,
                        fontSize = 14.sp,
                        color = Color(0xFF424242),
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )

                    if (notification.unread) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    }
                }
            }
        }
        
            // Línea divisoria entre notificaciones
            Divider(
                color = Color(0xFFBDBDBD),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

/*
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
*/
