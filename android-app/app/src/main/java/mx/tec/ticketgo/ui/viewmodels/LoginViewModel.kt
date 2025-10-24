package mx.tec.ticketgo.ui.viewmodels

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.network.TokenStorage
import mx.tec.ticketgo.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository = AuthRepository()): BaseViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    private val _userRole = MutableStateFlow<Int?>(null)

    fun login(email: String, password: String, context: Context) {
        val request = LoginRequest(email, password)

        safeCall(
            action = { repository.login(request) },
            onSuccess = {
                _token.value = it.token
                _message.value = it.message
                _userRole.value = it.user.rol

                viewModelScope.launch {
                    TokenStorage.saveToken(it.token)
                    TokenStorage.saveUserName(it.user.nombre)
                }
                val sharedPref =
                    context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                sharedPref.edit {
                    putString("auth_token", it.token)
                    putInt("id_user", it.user.id)
                    putInt("id_role", it.user.rol)
                    putString("user_name", it.user.nombre)
                }
            }
        )
    }

    fun logout(context: Context, onLogoutSuccess: () -> Unit = {}) {
        safeCall(
            action = { repository.logout() },
            onSuccess = {
                _message.value = it.message
                // Limpiar SharedPreferences específicamente
                val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                sharedPref.edit {
                    remove("auth_token")
                    remove("id_user")
                    remove("id_role")
                    remove("user_name")
                    apply()
                }
                // Limpiar DataStore
                viewModelScope.launch {
                    TokenStorage.clearToken()
                }
                // Limpiar estados locales
                _token.value = null
                _userRole.value = null
                // Ejecutar callback de éxito
                onLogoutSuccess()
            }
        )
    }

    fun clearMessage() {
        _message.value = null
    }
}