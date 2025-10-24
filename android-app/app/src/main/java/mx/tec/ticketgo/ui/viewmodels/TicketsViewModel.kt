package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.AcceptTicketRequest
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
        category: String? = null,
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
            onSuccess = { tickets ->
                println("📥 Tickets recibidos: ${tickets.size}")
                tickets.forEach { ticket ->
                    println("  - Ticket ${ticket.id_ticket}: aceptado = ${ticket.aceptado}")
                }
                
                // 🔹 Preservar cambios locales importantes al actualizar desde el servidor
                val currentTickets = _tickets.value
                val mergedTickets = tickets.map { serverTicket ->
                    val localTicket = currentTickets.find { it.id_ticket == serverTicket.id_ticket }
                    
                    // Si existe localmente y tiene cambios importantes, preservarlos
                    if (localTicket != null) {
                        // Preservar el campo 'aceptado' si fue modificado localmente y el servidor aún no lo tiene
                        val preservedAceptado = if (localTicket.aceptado != null && serverTicket.aceptado == null) {
                            println("  ⚡ Preservando aceptado=${localTicket.aceptado} para ticket ${serverTicket.id_ticket}")
                            localTicket.aceptado
                        } else {
                            serverTicket.aceptado
                        }
                        
                        serverTicket.copy(aceptado = preservedAceptado)
                    } else {
                        serverTicket
                    }
                }
                
                _tickets.value = mergedTickets
                println("✅ Tickets actualizados con cambios locales preservados")
            }
        )
    }

    fun getTicketById(id: Int){
        safeCall(
            action = { repository.getTicketById(id) },
            onSuccess = { _ticket.value = it}
        )
    }

    fun resetTicketId() {
        _ticketId.value = null
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

    fun assignTicket(id: Int, technicianId: Int, technicianName: String? = null){
        val request = AssignTicketRequest(technicianId)
        safeCall(
            action = { repository.assignTicket(id, request) },
            onSuccess = { 
                _message.value = it.message
                // Actualizar localmente el ticket asignado
                updateTicketTechnicianLocally(id, technicianId, technicianName)
            }
        )
    }
    
    // Método para actualizar el técnico asignado localmente
    fun updateTicketTechnicianLocally(ticketId: Int, technicianId: Int, technicianName: String? = null) {
        val currentTickets = _tickets.value.toMutableList()
        val ticketIndex = currentTickets.indexOfFirst { it.id_ticket == ticketId }
        
        if (ticketIndex != -1) {
            // Usar el nombre del técnico si se proporciona, sino usar un placeholder
            val technicianDisplayName = technicianName ?: "Técnico Asignado"
            val updatedTicket = currentTickets[ticketIndex].copy(asignado_a = technicianDisplayName)
            currentTickets[ticketIndex] = updatedTicket
            _tickets.value = currentTickets
            
            // También actualizar el ticket individual si es el mismo
            if (_ticket.value?.id_ticket == ticketId) {
                _ticket.value = updatedTicket
            }
        }
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

    // Método para actualizar el estado del ticket (API + local)
    fun updateTicketStatus(ticketId: Int, statusId: Int) {
        // Primero actualizar localmente para respuesta inmediata
        val statusName = when (statusId) {
            1 -> "Abierto"
            2 -> "En Progreso"
            3 -> "Cerrado"
            4 -> "Resuelto"
            5 -> "Reabierto"
            else -> "Abierto"
        }
        updateTicketStatusLocally(ticketId, statusName)
        
        // Luego hacer la llamada a la API
        changeTicketState(ticketId, statusId)
    }

    // Método para actualizar el estado del ticket localmente (sin llamada a API)
    fun updateTicketStatusLocally(ticketId: Int, newStatus: String) {
        val currentTickets = _tickets.value.toMutableList()
        val ticketIndex = currentTickets.indexOfFirst { it.id_ticket == ticketId }
        
        if (ticketIndex != -1) {
            val updatedTicket = currentTickets[ticketIndex].copy(estado = newStatus)
            currentTickets[ticketIndex] = updatedTicket
            _tickets.value = currentTickets
            
            // También actualizar el ticket individual si es el mismo
            if (_ticket.value?.id_ticket == ticketId) {
                _ticket.value = updatedTicket
            }
        }
    }

    // Método para actualizar la prioridad del ticket localmente
    fun updateTicketPriorityLocally(ticketId: Int, newPriority: String) {
        val currentTickets = _tickets.value.toMutableList()
        val ticketIndex = currentTickets.indexOfFirst { it.id_ticket == ticketId }
        
        if (ticketIndex != -1) {
            val updatedTicket = currentTickets[ticketIndex].copy(prioridad = newPriority)
            currentTickets[ticketIndex] = updatedTicket
            _tickets.value = currentTickets
            
            // También actualizar el ticket individual si es el mismo
            if (_ticket.value?.id_ticket == ticketId) {
                _ticket.value = updatedTicket
            }
        }
    }

    // Método para actualizar la categoría del ticket localmente
    fun updateTicketCategoryLocally(ticketId: Int, newCategory: String) {
        val currentTickets = _tickets.value.toMutableList()
        val ticketIndex = currentTickets.indexOfFirst { it.id_ticket == ticketId }
        
        if (ticketIndex != -1) {
            val updatedTicket = currentTickets[ticketIndex].copy(categoria = newCategory)
            currentTickets[ticketIndex] = updatedTicket
            _tickets.value = currentTickets
            
            // También actualizar el ticket individual si es el mismo
            if (_ticket.value?.id_ticket == ticketId) {
                _ticket.value = updatedTicket
            }
        }
    }

    // Método para aceptar o rechazar un ticket
    fun acceptTicket(id: Int, accepted: Boolean, onRejected: () -> Unit = {}){
        println("🚀 TicketsViewModel.acceptTicket() llamado - ID: $id, Aceptado: $accepted")
        val request = AcceptTicketRequest(accepted)
        safeCall(
            action = { 
                println("📡 Llamando a repository.acceptTicket...")
                repository.acceptTicket(id, request) 
            },
            onSuccess = { 
                println("✅ API respondió exitosamente: ${it.message}")
                _message.value = it.message
                
                if (accepted) {
                    // Si fue aceptado, actualizar localmente (1 = aceptado)
                    updateTicketAcceptedLocally(id, 1)
                } else {
                    // Si fue rechazado, eliminar de la lista local
                    println("🗑️ Ticket rechazado, eliminando de la lista local")
                    removeTicketLocally(id)
                    // Ejecutar callback para navegar hacia atrás
                    onRejected()
                }
            }
        )
    }
    
    // Método para eliminar un ticket de la lista local
    private fun removeTicketLocally(ticketId: Int) {
        val currentTickets = _tickets.value.toMutableList()
        currentTickets.removeAll { it.id_ticket == ticketId }
        _tickets.value = currentTickets
        println("✅ Ticket $ticketId eliminado de la lista local")
    }

    // Método para actualizar el estado de aceptación del ticket localmente
    fun updateTicketAcceptedLocally(ticketId: Int, accepted: Int?) {
        println("🔄 Actualizando ticket localmente - ID: $ticketId, Aceptado: $accepted")
        val currentTickets = _tickets.value.toMutableList()
        val ticketIndex = currentTickets.indexOfFirst { it.id_ticket == ticketId }
        
        if (ticketIndex != -1) {
            val updatedTicket = currentTickets[ticketIndex].copy(aceptado = accepted)
            currentTickets[ticketIndex] = updatedTicket
            _tickets.value = currentTickets
            println("✅ Ticket actualizado en la lista - aceptado: ${updatedTicket.aceptado}")
            
            // También actualizar el ticket individual si es el mismo
            if (_ticket.value?.id_ticket == ticketId) {
                _ticket.value = updatedTicket
                println("✅ Ticket individual también actualizado")
            }
        } else {
            println("⚠️ No se encontró el ticket con ID: $ticketId")
        }
    }

    // Método para limpiar la lista de tickets (útil al cambiar entre historial e inicio)
    fun clearTickets() {
        println("🧹 Limpiando lista de tickets")
        _tickets.value = emptyList()
    }
}