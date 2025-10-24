package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.AcceptTicketRequest
import mx.tec.ticketgo.data.models.AssignTicketRequest
import mx.tec.ticketgo.data.models.ChangeTicketCategoryRequest
import mx.tec.ticketgo.data.models.ChangeTicketPriorityRequest
import mx.tec.ticketgo.data.models.ChangeTicketStateRequest
import mx.tec.ticketgo.data.models.CreateTicketRequest
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.data.models.CreateTicketResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketService {
    @GET("tickets/")
    suspend fun getTickets(
        @Query("estado") state: String? = null,
        @Query("prioridad") priority: Int? = null,
        @Query("tecnico") technician: Int? = null,
        @Query("categoria") category: String? = null,
        @Query("fecha_inicio") startDate: String? = null,
        @Query("fecha_fin") limitDate: String? = null
    ): Response<List<Ticket>>

    @GET("tickets/{id}")
    suspend fun getTicketById(@Path("id") id: Int): Response<Ticket>

    @POST("tickets/")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<CreateTicketResponse>

    @PUT("tickets/{id}")
    suspend fun updateTicket(
        @Path("id") id: Int,
        @Body request: CreateTicketRequest
    ): Response<GenericResponse>

    @DELETE("tickets/{id}")
    suspend fun deleteTicket(@Path("id") id: Int): Response<GenericResponse>

    @PUT("tickets/{id}/assign")
    suspend fun assignTicket(
        @Path("id") id: Int,
        @Body request: AssignTicketRequest
    ): Response<GenericResponse>

    @PUT("tickets/{id}/state")
    suspend fun changeTicketState(
        @Path("id") id: Int,
        @Body request: ChangeTicketStateRequest
    ): Response<GenericResponse>

    @PUT("tickets/{id}/priority")
    suspend fun changeTicketPriority(
        @Path("id") id: Int,
        @Body request: ChangeTicketPriorityRequest
    ): Response<GenericResponse>

    @PUT("tickets/{id}/category")
    suspend fun changeTicketCategory(
        @Path("id") id: Int,
        @Body request: ChangeTicketCategoryRequest
    ): Response<GenericResponse>

    @PUT("tickets/{id}/accept")
    suspend fun acceptTicket(
        @Path("id") id: Int,
        @Body request: AcceptTicketRequest
    ): Response<GenericResponse>
}