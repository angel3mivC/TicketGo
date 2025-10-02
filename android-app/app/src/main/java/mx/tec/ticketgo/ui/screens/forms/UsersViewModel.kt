package mx.tec.ticketgo.ui.screens.forms

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import mx.tec.ticketgo.data.repository.UserRepository

class UsersViewModel(application: Application): AndroidViewModel(application) {
    private val repository = UserRepository(application)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)

    fun getRoleId(role: String): Int {
        when(role){
            "tecnico" -> return 1
            "mesa de trabajo" -> return 2
        }
        return 0
    }

    fun createUser(
        name: String,
        email: String,
        password: String,
        role: String
    ){
        isLoading = true
        successMessage = null
        errorMessage = null

        val roleId = getRoleId(role)

        repository.createUser(
            name,
            email,
            password,
            roleId,
            {
                isLoading = true
                successMessage = it
            },{
                isLoading = false
                errorMessage = it
            }
        )
    }

    fun editUser(userId: Int, name: String, email: String, password: String, role: String){
        isLoading = true
        successMessage = null
        errorMessage = null

        val roleId = getRoleId(role)

        repository.editUser(
            userId,
            name,
            email,
            password,
            roleId,
            {
                isLoading = true
                successMessage = it
            },
            {
                isLoading = false
                errorMessage = it
            }
        )
    }
}