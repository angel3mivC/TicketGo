package mx.tec.ticketgo.data.repository

import mx.tec.ticketgo.data.models.File
import mx.tec.ticketgo.data.models.GenericResponse
import mx.tec.ticketgo.data.models.UploadFileResponse
import mx.tec.ticketgo.data.network.ApiClient
import mx.tec.ticketgo.data.remote.FilesService
import mx.tec.ticketgo.data.utils.safeApiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FilesRepository {
    private val service: FilesService = ApiClient.retrofit.create(FilesService::class.java)

    suspend fun getTickets(id: Int): Result<List<File>> =
        safeApiCall { service.getTickets(id) }

    suspend fun uploadFile(ticketId: Int, filePart: MultipartBody.Part, fileName: RequestBody, fileType: RequestBody): Result<UploadFileResponse> =
        safeApiCall { service.uploadFile(ticketId, filePart, fileName, fileType) }

    suspend fun deleteTicket(ticketId: Int, fileId: Int): Result<GenericResponse> =
        safeApiCall { service.deleteTicket(ticketId, fileId) }
}