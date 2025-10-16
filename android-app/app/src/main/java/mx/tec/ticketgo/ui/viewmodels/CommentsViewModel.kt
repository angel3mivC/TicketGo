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
                id_comentario = 0,
                comentario = commentText,
                fecha = "Ahora",
                autor = userName
            )
            
            // 2. Agregar inmediatamente a la lista (optimistic update)
            _comments.value = _comments.value + tempComment
            val request = CreateCommentRequest(commentText)
            try {
                println("🔍 DEBUG: Enviando comentario para ticket $ticketId con texto: '$commentText'")
                println("🔍 DEBUG: Request: $request")
                
                val result = repository.createComment(ticketId, request)
                result.onSuccess { response ->
                    println("✅ DEBUG: Comentario creado exitosamente con ID: ${response.id_comentario}")
                    // 4. Actualizar el comentario temporal con el ID real y datos del servidor
                    val updatedComment = tempComment.copy(
                        id_comentario = response.id_comentario,
                        fecha = "Enviado", // O usar fecha real del servidor si está disponible
                        autor = userName
                    )
                    
                    // Reemplazar el comentario temporal con el real
                    _comments.value = _comments.value.map { comment ->
                        if (comment == tempComment) updatedComment else comment
                    }
                    
                    _message.value = response.message
                }
                result.onFailure { error ->
                    println("❌ DEBUG: Error al crear comentario: ${error.message}")
                    // 5. En caso de error, remover el comentario temporal
                    _comments.value = _comments.value - tempComment
                    _message.value = "Error al enviar comentario: ${error.message}"
                }
            } catch (e: Exception) {
                println("💥 DEBUG: Excepción no controlada: ${e.message}")
                // 6. Manejar excepciones no controladas
                _comments.value = _comments.value - tempComment
                _message.value = "Error al enviar comentario: ${e.message}"
            }
        }
    }

}