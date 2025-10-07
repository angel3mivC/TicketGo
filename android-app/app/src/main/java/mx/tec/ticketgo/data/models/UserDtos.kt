package mx.tec.ticketgo.data.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val roleId: Int,
    val state: String
)

data class UserPrueba(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: Int
)

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val roleId: Int
)

data class CreateUserResponse(
    val message: String,
    val user: User
)