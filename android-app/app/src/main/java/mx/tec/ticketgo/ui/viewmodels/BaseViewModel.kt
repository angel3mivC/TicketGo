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

    protected fun <T> safeCall(
        action: suspend () -> Result<T>,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = action()
                result.onSuccess(onSuccess)
                result.onFailure { e ->
                    _message.value = try {
                        e.message?.let {
                            JSONObject(it).optString("message", it)
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
