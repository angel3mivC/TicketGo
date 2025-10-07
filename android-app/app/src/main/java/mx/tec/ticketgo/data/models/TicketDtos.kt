package mx.tec.ticketgo.data.models

data class Ticket(
    val title: String,
    val description: String,
    val categoryId: Int,
    val priorityId: Int
)

data class createTicketResponse(
    val message: String,
    val ticketId: Int
)