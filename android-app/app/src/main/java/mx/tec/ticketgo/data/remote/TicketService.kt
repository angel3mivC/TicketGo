package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.data.models.createTicketResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketService {
    @GET("/tickets/")
    suspend fun getTickets(
        @Query("state") state: String? = null,
        @Query("priority") priority: String? = null,
        @Query("technician") technician: Int? = null,
        @Query("category") category: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("limit_date") limitDate: String? = null
    ): Response<List<Ticket>>

    @GET("/tickets/{id}")
    suspend fun getTicketById(@Path("id") id: Int): Response<Ticket>

    @POST("/tickets/")
    suspend fun createTicket(@Body ticket: Ticket): Response<createTicketResponse>

    @PUT("/tickets/{id}")
    suspend fun updateTicket(
        @Path("id") id: Int,
        @Body ticket: Ticket
    ): Response<GenericResponse>

    @DELETE("/tickets/{id}")
    suspend fun deleteTicket(@Path("id") id: Int): Response<GenericResponse>

    @PUT("/tickets/{id}/assign")
    suspend fun assignTicket(
        @Path("id") id: Int,
        @Body technicianId: Int
    ): Response<GenericResponse>

    @PUT("/tickets/{id}/state")
    suspend fun changeTicketState(
        @Path("id") id: Int,
        @Body stateID: Int
    ): Response<GenericResponse>

    @PUT("/tickets/{id}/priority")
    suspend fun changeTicketPriority(
        @Path("id") id: Int,
        @Body priorityId: Int
    ): Response<GenericResponse>

    @PUT("/tickets/{id}/category")
    suspend fun changeTicketCategory(
        @Path("id") id: Int,
        @Body categoryId: Int
    ): Response<GenericResponse>
}