package mx.tec.ticketgo.data.models

data class Ticket(
    val id_ticket: Int,
    val titulo: String,
    val descripcion: String,
    val fecha_creacion: String,
    val fecha_actualizacion: String?,
    val creado_por: String?,
    val asignado_a: String?,
    val categoria: String?,
    val prioridad: String?,
    val estado: String?,
    val aceptado: Int? // 1 = aceptado, 0 = rechazado, null = pendiente
)

data class TicketFilterRequest(
    val estado: String? = null,
    val prioridad: Int? = null,
    val tecnico: Int? = null,
    val categoria: String? = null,
    val fecha_inicio: String? = null,
    val fecha_fin: String? = null
)

data class CreateTicketRequest(
    val titulo: String,
    val descripcion: String,
    val id_categoria: Int,
    val id_prioridad: Int
)
data class CreateTicketResponse(
    val message: String,
    val ticket_id: Int
)

data class AssignTicketRequest(
    val tecnico_id: Int
)

data class ChangeTicketStateRequest(
    val estado_id: Int
)

data class ChangeTicketPriorityRequest(
    val prioridad_id: Int
)

data class ChangeTicketCategoryRequest(
    val categoria_id: Int
)

data class AcceptTicketRequest(
    val aceptado: Boolean
)