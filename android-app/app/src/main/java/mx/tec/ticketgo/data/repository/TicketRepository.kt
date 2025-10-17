package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.AssignTicketRequest
import mx.tec.ticketgo.data.models.ChangeTicketCategoryRequest
import mx.tec.ticketgo.data.models.ChangeTicketPriorityRequest
import mx.tec.ticketgo.data.models.ChangeTicketStateRequest
import mx.tec.ticketgo.data.models.CreateTicketRequest
import mx.tec.ticketgo.data.models.CreateTicketResponse
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.TicketFilterRequest
import mx.tec.ticketgo.data.remote.TicketService
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.utils.safeApiCall

class TicketRepository() {
    private val service: TicketService = ApiClient.retrofit.create(TicketService::class.java)

    suspend fun getTickets(request: TicketFilterRequest): Result<List<Ticket>> =
        safeApiCall { service.getTickets(
            request.estado,
            request.prioridad,
            request.tecnico,
            request.categoria,
            request.fecha_inicio,
            request.fecha_fin
        ) }

    suspend fun getTicketById(id: Int): Result<Ticket> =
        safeApiCall { service.getTicketById(id) }

    suspend fun createTicket(request: CreateTicketRequest): Result<CreateTicketResponse> =
        safeApiCall { service.createTicket(request) }

    suspend fun updateTicket(id: Int, request: CreateTicketRequest): Result<GenericResponse> =
        safeApiCall { service.updateTicket(id, request) }

    suspend fun deleteTicket(id: Int): Result<GenericResponse> =
        safeApiCall { service.deleteTicket(id) }

    suspend fun assignTicket(id: Int, request: AssignTicketRequest): Result<GenericResponse> =
        safeApiCall { service.assignTicket(id, request) }

    suspend fun changeTicketState(id: Int, request: ChangeTicketStateRequest): Result<GenericResponse> =
        safeApiCall { service.changeTicketState(id, request) }

    suspend fun changeTicketPriority(id: Int, request: ChangeTicketPriorityRequest): Result<GenericResponse> =
        safeApiCall { service.changeTicketPriority(id, request) }

    suspend fun changeTicketCategory(id: Int, request: ChangeTicketCategoryRequest): Result<GenericResponse> =
        safeApiCall { service.changeTicketCategory(id, request) }
}