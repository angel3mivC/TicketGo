package mx.tec.ticketgo.ui.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.tec.ticketgo.data.models.Comment
import mx.tec.ticketgo.data.models.CreateCommentRequest
import mx.tec.ticketgo.data.network.TokenStorage
import mx.tec.ticketgo.data.repository.CommentsRepository

class CommentsViewModel(private val repository: CommentsRepository = CommentsRepository()): BaseViewModel() {

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

    fun addLocalComment(ticketId: Int, commentText: String) {
        // 3. Llamar a la API para crear el comentario real
        viewModelScope.launch {
            // Obtener el nombre de usuario guardado
            val userName = TokenStorage.getUserName() ?: "Usuario"
            
            // 1. Crear comentario temporal para mostrar inmediatamente
            val tempComment = Comment(
                id_comentario = -1, // ID negativo para identificar comentarios temporales
                comentario = commentText,
                fecha = "Enviando...",
                autor = userName
            )
            
            // 2. Agregar inmediatamente a la lista (optimistic update)
            val currentComments = _comments.value.toMutableList()
            currentComments.add(tempComment)
            _comments.value = currentComments
            
            val request = CreateCommentRequest(commentText)
            try {
                val result = repository.createComment(ticketId, request)
                result.onSuccess { response ->
                    // 4. Actualizar el comentario temporal con el ID real y datos del servidor
                    val updatedComment = Comment(
                        id_comentario = response.id_comentario,
                        comentario = commentText,
                        fecha = "Enviado",
                        autor = userName
                    )
                    
                    // Reemplazar el comentario temporal con el real usando índice
                    val updatedComments = _comments.value.toMutableList()
                    val tempIndex = updatedComments.indexOfFirst { it.id_comentario == -1 && it.comentario == commentText }
                    if (tempIndex != -1) {
                        updatedComments[tempIndex] = updatedComment
                        _comments.value = updatedComments
                    }
                    
                    _message.value = response.message
                }
                result.onFailure { error ->
                    // 5. En caso de error, remover el comentario temporal
                    val updatedComments = _comments.value.toMutableList()
                    updatedComments.removeAll { it.id_comentario == -1 && it.comentario == commentText }
                    _comments.value = updatedComments
                    
                    // Mostrar mensaje de error más específico
                    val errorMessage = when {
                        error.message?.contains("403") == true -> "No tienes permisos para comentar en este ticket"
                        error.message?.contains("401") == true -> "Sesión expirada. Por favor, inicia sesión nuevamente"
                        error.message?.contains("404") == true -> "Ticket no encontrado"
                        else -> "Error al enviar comentario: ${error.message}"
                    }
                    _message.value = errorMessage
                }
            } catch (e: Exception) {
                // 6. Manejar excepciones no controladas
                val updatedComments = _comments.value.toMutableList()
                updatedComments.removeAll { it.id_comentario == -1 && it.comentario == commentText }
                _comments.value = updatedComments
                _message.value = "Error al enviar comentario: ${e.message}"
            }
        }
    }

}