package mx.tec.ticketgo.data.models

data class Evidence(
    val id: Int,
    val fileName: String,
    val fileType: String,
    val fileSize: Long,
    val uploadDate: String,
    val uploadTime: String,
    val fileUrl: String? = null,
    val thumbnailUrl: String? = null,
    val localPath: String? = null
)
