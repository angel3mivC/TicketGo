package mx.tec.ticketgo.data.remote

import mx.tec.ticketgo.data.models.File
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.UploadFileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FilesService {
    @GET("tickets/{id}/files/")
    suspend fun getTickets(@Path("id") id: Int): Response<List<File>>

    @Multipart
    @POST("tickets/{ticketId}/files/")
    suspend fun uploadFile(
        @Path("ticketId") ticketId: Int,
        @Part file: MultipartBody.Part,
        @Part("nombre_archivo") fileName: RequestBody,
        @Part("tipo_archivo") fileType: RequestBody
    ): Response<UploadFileResponse>

    @DELETE("tickets/{ticketId}/files/{fileId}")
    suspend fun deleteTicket(
        @Path("ticketId") ticketId: Int,
        @Path("fileId") fileId: Int
    ): Response<GenericResponse>
}