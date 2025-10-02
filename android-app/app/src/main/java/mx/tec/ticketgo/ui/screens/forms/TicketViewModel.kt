package mx.tec.ticketgo.ui.screens.forms

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import mx.tec.ticketgo.data.repository.TicketRepository

class TicketViewModel(application: Application): AndroidViewModel(application){
    private val repository = TicketRepository(application)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var ticketId by mutableStateOf<Int?>(null)
        private set

    fun addTicket(title: String, description: String, priority: String, category: String, status: String, comments: String){
        isLoading = true
        errorMessage = null
        ticketId = null

        var categoryId = 0
        when(category){
            "" -> categoryId = 1
        }

        var priorityId = 0
        when(priority){
            "" -> priorityId = 1
        }

        repository.createTicket(
            title,
            description,
            priorityId,
            categoryId,
            status,
            comments,
            {
                isLoading = false
                ticketId = it
            },
            {
                isLoading = false
                errorMessage = it
            }
        )
    }
}