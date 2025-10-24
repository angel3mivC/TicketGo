package mx.tec.ticketgo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

open class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    protected val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    protected val _error = MutableStateFlow<Boolean>(false)
    val error = _error.asStateFlow()

    protected fun <T> safeCall(
        action: suspend () -> Result<T>,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            println("🔵 BaseViewModel.safeCall - Iniciando...")
            _isLoading.value = true
            try {
                val result = action()
                result.onSuccess {
                    println("🟢 BaseViewModel.safeCall - Éxito")
                    _error.value = false
                    onSuccess(it)
                }
                result.onFailure { e ->
                    println("🔴 BaseViewModel.safeCall - Error: ${e.message}")
                    _error.value = true
                    _message.value = try {
                        e.message?.let { message ->
                            // Buscar JSON en el mensaje (puede estar después de texto como "HTTP 401")
                            val jsonRegex = Regex("""\{[^}]*"message"[^}]*\}""")
                            val jsonMatch = jsonRegex.find(message)
                            
                            if (jsonMatch != null) {
                                // Extraer y parsear el JSON encontrado
                                try {
                                    JSONObject(jsonMatch.value).optString("message", message)
                                } catch (_: Exception) {
                                    message
                                }
                            } else {
                                // Si no hay JSON, limpiar prefijos comunes
                                when {
                                    message.startsWith("error->") -> message.substring(7)
                                    message.startsWith("HTTP") -> {
                                        // Para mensajes HTTP, intentar extraer la parte útil
                                        val parts = message.split(" - ")
                                        if (parts.size > 1) parts.last() else message
                                    }
                                    else -> message
                                }
                            }
                        } ?: "Unknown error"
                    } catch (_: Exception) {
                        e.message ?: "Unknown error"
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
