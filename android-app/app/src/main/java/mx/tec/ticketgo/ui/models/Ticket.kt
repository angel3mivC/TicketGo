package mx.tec.ticketgo.ui.models

data class Ticket(
    val ticketId: Int,
    val title: String,
    val description: String,
    val categoryId: Int,
    val priorityId: Int,
    val status: String,
    val tecnico: String? = null,
    val startDate: String
)