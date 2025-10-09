package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.ClosedTickets
import mx.tec.ticketgo.data.models.MyTickets
import mx.tec.ticketgo.data.models.OpenTickets
import mx.tec.ticketgo.data.models.Summary
import mx.tec.ticketgo.data.models.TotalTickets
import retrofit2.Response
import retrofit2.http.GET

interface DashboardService {
    @GET("dashboard/summary")
    suspend fun dashboardSummary(): Response<Summary>

    @GET("dashboard/total")
    suspend fun totalTickets(): Response<TotalTickets>

    @GET("dashboard/open")
    suspend fun openTickets(): Response<OpenTickets>

    @GET("dashboard/closed")
    suspend fun closedTickets(): Response<ClosedTickets>

    @GET("dashboard/my-tickets")
    suspend fun myTickets(): Response<MyTickets>
}