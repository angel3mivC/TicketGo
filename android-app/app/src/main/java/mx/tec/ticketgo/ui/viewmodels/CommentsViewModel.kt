package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.CreateCommentRequest
import mx.tec.ticketgo.data.repository.CommentsRepository

class CommentsViewModel(private val repository: CommentsRepository): BaseViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comment: StateFlow<List<Comment>> = _comments

    fun getComments(id: Int){
        safeCall(
            action = { repository.getComments(id) },
            onSuccess = { _comments.value = it }
        )
    }

    fun createComment(id: Int, comment: String){
        val request = CreateCommentRequest(comment)
        safeCall(
            action = { repository.createComment(id, request) },
            onSuccess = { _message.value = it.message }
        )
    }

}