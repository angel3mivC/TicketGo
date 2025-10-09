package mx.tec.ticketgo.data.models

data class RolesResponse(
    val id_rol: Int,
    val nombre: String
)

data class StatesResponse(
    val id_estado: Int,
    val nombre: String
)

data class CategoryResponse(
    val id_categoria: Int,
    val nombre: String
)

data class PriorityResponse(
    val id_prioridad: Int,
    val nivel: String
)