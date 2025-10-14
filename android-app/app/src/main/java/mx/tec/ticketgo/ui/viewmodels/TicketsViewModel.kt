package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.AssignTicketRequest
import mx.tec.ticketgo.data.models.ChangeTicketCategoryRequest
import mx.tec.ticketgo.data.models.ChangeTicketPriorityRequest
import mx.tec.ticketgo.data.models.ChangeTicketStateRequest
import mx.tec.ticketgo.data.models.CreateTicketRequest
import mx.tec.ticketgo.data.models.Ticket
import mx.tec.ticketgo.data.models.TicketFilterRequest
import mx.tec.ticketgo.data.repository.TicketRepository

class TicketsViewModel(private val repository: TicketRepository = TicketRepository()): BaseViewModel() {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    private val _ticket = MutableStateFlow<Ticket?>(null)
    val ticket: StateFlow<Ticket?> = _ticket

    private val _ticketId = MutableStateFlow<Int?>(null)
    val ticketId: StateFlow<Int?> = _ticketId

    fun getTickets(
        state: String? = null,
        priority: Int? = null,
        technician: Int? = null,
        category: Int? = null,
        startDate: String? = null,
        endDate: String? = null
    ){
        val request = TicketFilterRequest(
            state,
            priority,
            technician,
            category,
            startDate,
            endDate
        )
        safeCall(
            action = { repository.getTickets(request) },
            onSuccess = { _tickets.value = it}
        )
    }

    fun getTicketById(id: Int){
        safeCall(
            action = { repository.getTicketById(id) },
            onSuccess = { _ticket.value = it}
        )
    }

    fun createTicket(title: String, description: String, categoryId: Int, priorityId: Int){
        val request = CreateTicketRequest(title, description, categoryId, priorityId)
        safeCall(
            action = { repository.createTicket(request) },
            onSuccess = {
                _message.value = it.message
                _ticketId.value = it.ticket_id
            }
        )
    }

    fun updateTicket(id: Int, title: String, description: String, categoryId: Int, priorityId: Int){
        val request = CreateTicketRequest(title, description, categoryId, priorityId)
        safeCall(
            action = { repository.updateTicket(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun deleteTicket(id: Int){
        safeCall(
            action = { repository.deleteTicket(id) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun assignTicket(id: Int, technicianId: Int){
        val request = AssignTicketRequest(technicianId)
        safeCall(
            action = { repository.assignTicket(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun changeTicketState(id: Int, stateId: Int){
        val request = ChangeTicketStateRequest(stateId)
        safeCall(
            action = { repository.changeTicketState(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun changeTicketPriority(id: Int, priorityId: Int){
        val request = ChangeTicketPriorityRequest(priorityId)
        safeCall(
            action = { repository.changeTicketPriority(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun changeTicketCategory(id: Int, categoryId: Int){
        val request = ChangeTicketCategoryRequest(categoryId)
        safeCall(
            action = { repository.changeTicketCategory(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }
}