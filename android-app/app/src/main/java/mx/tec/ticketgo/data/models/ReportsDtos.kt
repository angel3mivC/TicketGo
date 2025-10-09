package mx.tec.ticketgo.data.models

data class TicketReportByState(
    val estado: String,
    val total: Int
)

data class TicketReportByPriority(
    val prioridad: String,
    val total: Int
)

data class TicketReportByCategory(
    val categoria: String,
    val total: Int
)

data class TicketReportByTechnician(
    val nombre: String,
    val total: Int
)

data class TicketReportByDateRequest(
    val inicio: String?,
    val fin: String?
)

data class TicketReportByDate(
    val fecha: String,
    val total: Int
)
