package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.Notification
import mx.tec.ticketgo.data.repository.NotificationsRepository

class NotificationsViewModel(private val repository: NotificationsRepository = NotificationsRepository()): BaseViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications


    fun getNotifications(){
        // Ahora que tenemos autenticación funcionando, cargar notificaciones reales
        println("🔔 NotificationsViewModel: Iniciando carga de notificaciones")
        safeCall(
            action = { 
                println("🔔 NotificationsViewModel: Llamando a repository.getNotifications()")
                repository.getNotifications() 
            },
            onSuccess = { notifs ->
                println("🔔 NotificationsViewModel: Notificaciones recibidas: ${notifs.size}")
                // Ordenar: no leídas (leido = 0) primero, luego las leídas
                val sorted = notifs.sortedBy { it.leido }
                _notifications.value = sorted
            }
        )
    }

    fun markAsRead(id: Int){
        println("📖 Marcando notificación $id como leída")
        // Primero actualizar localmente para feedback inmediato
        markAsReadLocally(id)
        
        // Luego llamar al API
        safeCall(
            action = { repository.markAsRead(id) },
            onSuccess = { 
                println("✅ Notificación marcada como leída en el servidor")
                _message.value = it.message 
            }
        )
    }
    
    // Método para actualizar localmente el estado de leído
    private fun markAsReadLocally(id: Int) {
        val currentNotifications = _notifications.value.toMutableList()
        val index = currentNotifications.indexOfFirst { it.id_notificacion == id }
        
        if (index != -1) {
            val updatedNotification = currentNotifications[index].copy(leido = 1)
            currentNotifications[index] = updatedNotification
            
            // Reordenar: no leídas primero
            val sorted = currentNotifications.sortedBy { it.leido }
            _notifications.value = sorted
            println("✅ Notificación $id marcada como leída localmente")
        }
    }
}