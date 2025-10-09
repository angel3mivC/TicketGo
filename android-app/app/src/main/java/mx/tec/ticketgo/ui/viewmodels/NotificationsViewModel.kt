package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.Notification
import mx.tec.ticketgo.data.repository.NotificationsRepository

class NotificationsViewModel(private val repository: NotificationsRepository): BaseViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    fun getNotifications(){
        safeCall(
            action = { repository.getNotifications() },
            onSuccess = { _notifications.value = it }
        )
    }

    fun markAsRead(id: Int){
        safeCall(
            action = { repository.markAsRead(id) },
            onSuccess = { _message.value = it.message }
        )
    }
}