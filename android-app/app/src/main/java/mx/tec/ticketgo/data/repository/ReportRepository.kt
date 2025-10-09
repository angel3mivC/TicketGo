package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.TicketReportByCategory
import mx.tec.ticketgo.data.models.TicketReportByDate
import mx.tec.ticketgo.data.models.TicketReportByDateRequest
import mx.tec.ticketgo.data.models.TicketReportByPriority
import mx.tec.ticketgo.data.models.TicketReportByState
import mx.tec.ticketgo.data.models.TicketReportByTechnician
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.ReportsService
import mx.tec.ticketgo.data.utils.safeApiCall

class ReportRepository() {
    private val service: ReportsService = ApiClient.retrofit.create(ReportsService::class.java)

    suspend fun reportByState(): Result<List<TicketReportByState>> =
        safeApiCall { service.reportByState() }

    suspend fun reportByPriority(): Result<List<TicketReportByPriority>> =
        safeApiCall { service.reportByPriority() }

    suspend fun reportByCategory(): Result<List<TicketReportByCategory>> =
        safeApiCall { service.reportByCategory() }

    suspend fun reportByTechnician(): Result<List<TicketReportByTechnician>> =
        safeApiCall { service.reportByTechnician() }

    suspend fun reportByData(request: TicketReportByDateRequest): Result<List<TicketReportByDate>> =
        safeApiCall {
            service.reportByDate(
                request.inicio,
                request.fin
            )
        }
}