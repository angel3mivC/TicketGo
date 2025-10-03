package mx.tec.ticketgo.data.repository

data class Ticket(
    val ticketId: Int,
    val title: String,
    val description: String,
    val categoryId: Int,
    val priorityId: Int,
    val status: String,
    val tecnico: String? = null,
)