package mx.tec.ticketgo.data.models

data class Notification(
    val id_notificacion: Int,
    val id_ticket: Int,
    val mensaje: String,
    val leido: Boolean,
    val fecha: String
)