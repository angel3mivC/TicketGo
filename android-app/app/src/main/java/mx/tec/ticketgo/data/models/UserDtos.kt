package mx.tec.ticketgo.data.models

data class GetUserResponse(
    val id_usuario: Int,
    val nombre: String,
    val correo: String,
    val id_rol: Int,
    val estado: String,
    val fecha_creacion: String
)

data class User(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: Int
)

data class CreateUserRequest(
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val id_rol: Int
)

data class CreateUserResponse(
    val message: String,
    val user: User
)