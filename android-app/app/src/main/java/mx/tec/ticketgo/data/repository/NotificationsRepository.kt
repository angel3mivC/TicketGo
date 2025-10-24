package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.Notification
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.NotificationsService
import mx.tec.ticketgo.data.utils.safeApiCall

class NotificationsRepository() {
    private val service: NotificationsService = ApiClient.retrofit.create(NotificationsService::class.java)

    suspend fun getNotifications(): Result<List<Notification>> {
        println("🔔 NotificationsRepository: Iniciando llamada a API")
        return safeApiCall { 
            println("🔔 NotificationsRepository: Llamando a service.getNotifications()")
            service.getNotifications() 
        }
    }

    suspend fun markAsRead(id: Int): Result<GenericResponse> =
        safeApiCall { service.markAsRead(id) }
}