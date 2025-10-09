package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.ClosedTickets
import mx.tec.ticketgo.data.models.MyTickets
import mx.tec.ticketgo.data.models.OpenTickets
import mx.tec.ticketgo.data.models.Summary
import mx.tec.ticketgo.data.models.TotalTickets
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.DashboardService
import mx.tec.ticketgo.data.utils.safeApiCall

class DashboardRepository() {
    private val service: DashboardService = ApiClient.retrofit.create(DashboardService::class.java)

    suspend fun dashboardSummary(): Result<Summary> =
        safeApiCall { service.dashboardSummary() }

    suspend fun totalTickets(): Result<TotalTickets> =
        safeApiCall { service.totalTickets() }

    suspend fun openTickets(): Result<OpenTickets> =
        safeApiCall { service.openTickets() }

    suspend fun closedTickets(): Result<ClosedTickets> =
        safeApiCall { service.closedTickets() }

    suspend fun myTickets(): Result<MyTickets> =
        safeApiCall { service.myTickets() }
}