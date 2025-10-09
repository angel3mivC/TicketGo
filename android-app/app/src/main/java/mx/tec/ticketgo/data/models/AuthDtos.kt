package mx.tec.ticketgo.data.models

data class LoginRequest(
    val correo: String,
    val contraseña: String
)

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User
)

// problablemente hay que cambiar el user