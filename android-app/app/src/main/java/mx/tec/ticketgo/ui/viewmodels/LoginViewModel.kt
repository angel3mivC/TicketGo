package mx.tec.ticketgo.ui.viewmodels

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.LoginRequest
import mx.tec.ticketgo.data.repository.AuthRepository

class LoginViewModel(private val repository: AuthRepository = AuthRepository()): BaseViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _userRole = MutableStateFlow<Int?>(null)
    val userRole: StateFlow<Int?> = _userRole

    fun login(email: String, password: String, context: Context) {
        val request = LoginRequest(email, password)

        safeCall(
            action = { repository.login(request) },
            onSuccess = {
                _token.value = it.token
                _message.value = it.message
                _userRole.value = it.user.id_rol

                val sharedPref =
                    context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                sharedPref.edit {
                    putString("auth_token", it.token)
                    putInt("id_usuario", it.user.id)
                }
            }
        )
    }

    fun logout(){}
}