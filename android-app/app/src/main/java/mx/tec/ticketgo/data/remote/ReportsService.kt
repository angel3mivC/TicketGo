package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.TicketReportByCategory
import mx.tec.ticketgo.data.models.TicketReportByDate
import mx.tec.ticketgo.data.models.TicketReportByPriority
import mx.tec.ticketgo.data.models.TicketReportByState
import mx.tec.ticketgo.data.models.TicketReportByTechnician
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportsService {

    @GET("reports/state")
    suspend fun reportByState(): Response<List<TicketReportByState>>

    @GET("reports/priority")
    suspend fun reportByPriority(): Response<List<TicketReportByPriority>>

    @GET("reports/category")
    suspend fun reportByCategory(): Response<List<TicketReportByCategory>>

    @GET("reports/technician")
    suspend fun reportByTechnician(): Response<List<TicketReportByTechnician>>

    @GET("reports/dates")
    suspend fun reportByDate(
        @Query("inicio") start: String? = null,
        @Query("fin") end: String? = null
    ): Response<List<TicketReportByDate>>
}