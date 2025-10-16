package mx.tec.ticketgo.data.models

data class File(
    val id_adjunto: Int,
    val nombre_archivo: String,
    val tipo_archivo: String,
    val nombre_original: String,
    val tipo_mime: String,
    val fecha: String
)

data class UploadFileRequest(
    val nombre_archivo: String,
    val tipo_archivo: String,
    val url_archivo: String
)

data class UploadFileResponse(
    val message: String,
    val id_adjunto: Int
)