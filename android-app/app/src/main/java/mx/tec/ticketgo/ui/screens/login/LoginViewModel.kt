package mx.tec.ticketgo.ui.screens.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import mx.tec.ticketgo.data.repository.AuthRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var token by mutableStateOf<String?>(null)
        private set

    fun login(username: String, password: String) {
        isLoading = true
        errorMessage = null
        token = null

        repository.Login(
            username,
            password,
            onSuccess = {
                isLoading = false
                token = it
            },
            onError = {
                isLoading = false
                errorMessage = it
            }
        )
    }
}
