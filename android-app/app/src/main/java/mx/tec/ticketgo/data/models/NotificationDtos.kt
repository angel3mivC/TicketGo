package mx.tec.ticketgo.data.models

data class Notification(
    val id_notificacion: Int,
    val id_ticket: Int,
    val mensaje: String,
    val leido: Int, // Cambiado a Int porque el backend devuelve 0/1
    val fecha: String
)