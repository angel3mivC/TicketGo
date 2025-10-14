package mx.tec.ticketgo.data.models

data class Comment(
    val id_comentario: Int,
    val comentario: String,
    val fecha: String,
    val autor: String
)

data class CreateCommentRequest(
    val comentario: String
)

data class CreateCommentResponse(
    val message: String,
    val id_comentario: Int
)