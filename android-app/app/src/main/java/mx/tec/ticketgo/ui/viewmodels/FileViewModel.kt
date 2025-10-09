package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.File
import mx.tec.ticketgo.data.models.UploadFileRequest
import mx.tec.ticketgo.data.repository.FilesRepository

class FileViewModel(private val repository: FilesRepository): BaseViewModel() {
    private val _files = MutableStateFlow<List<File>>(emptyList())
    val files: StateFlow<List<File>> = _files

    fun getTickets(id: Int){
        safeCall(
            action = { repository.getTickets(id) },
            onSuccess = { _files.value = it }
        )
    }

    fun uploadFile(fileName: String, fileType: String, fileUrl: String){
        val request = UploadFileRequest(fileName, fileType, fileUrl)
        safeCall(
            action = { repository.uploadFile(request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun deleteTicket(ticketId: Int, fileId: Int){
        safeCall(
            action = { repository.deleteTicket(ticketId, fileId) },
            onSuccess = { _message.value = it.message }
        )
    }
}