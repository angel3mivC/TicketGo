package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.File
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.UploadFileRequest
import mx.tec.ticketgo.data.models.UploadFileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FilesService {
    @GET("tickets/{id}/files/")
    suspend fun getTickets(@Path("id") id: Int): Response<List<File>>

    @PUT("tickets/{id}/files/")
    suspend fun uploadFile(@Body request: UploadFileRequest): Response<UploadFileResponse>

    @DELETE("tickets/{ticketId}/files/{fileId}")
    suspend fun deleteTicket(
        @Path("ticketId") ticketId: Int,
        @Path("fileId") fileId: Int
    ): Response<GenericResponse>
}