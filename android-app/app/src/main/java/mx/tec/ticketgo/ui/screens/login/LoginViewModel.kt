package mx.tec.ticketgo.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository = AuthRepository()): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _userRole = MutableStateFlow<Int?>(null)
    val userRole: StateFlow<Int?> = _userRole

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LoginRequest(email, password)
                val result = repository.login(request)

                result.onSuccess { response ->
                    _token.value = response.token
                    _message.value = response.message
                    _userRole.value = response.user.rol
                }

                result.onFailure { e ->
                    _message.value = e.message ?: "Error al iniciar sesión"
                }

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(){

    }
}
