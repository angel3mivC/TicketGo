package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationsService {
    @GET("notifications/")
    suspend fun getNotifications(): Response<List<Notification>>

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: Int): Response<GenericResponse>
}