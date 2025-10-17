package mx.tec.ticketgo.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.CreateUserRequest
import mx.tec.ticketgo.data.models.GetUserResponse
import mx.tec.ticketgo.data.repository.UserRepository
class UserViewModel(private val repository: UserRepository = UserRepository()): BaseViewModel() {

    private val _users = MutableStateFlow<List<GetUserResponse>>(emptyList())
    val users: StateFlow<List<GetUserResponse>> = _users

    private val _technicians = MutableStateFlow<List<GetUserResponse>>(emptyList())
    val technicians: StateFlow<List<GetUserResponse>> = _technicians

    private val _user = MutableStateFlow<GetUserResponse?>(null)
    val user: StateFlow<GetUserResponse?> = _user

    fun getUsers(){
        safeCall(
            action = { repository.getUsers() },
            onSuccess = { _users.value = it }
        )
    }

    fun getTechnicians(){
        safeCall(
            action = { repository.getTechnicians() },
            onSuccess = { _technicians.value = it }
        )
    }

    fun getUser(id: Int){
        safeCall(
            action = { repository.getUserById(id) },
            onSuccess = { _user.value = it}
        )
    }

    fun createUser(name: String, email: String, password: String, roleId: Int){
        val request = CreateUserRequest(name, email, password, roleId)
        safeCall(
            action = { repository.createUser(request) },
            onSuccess = { _message.value = it.message }
        )
    }

    fun updateUser(id: Int, name: String, email: String, password: String, roleId: Int){
        val request = CreateUserRequest(name, email, password, roleId)
        safeCall(
            action = { repository.updateUser(id, request) },
            onSuccess = { _message.value = it.message}
        )
    }

    fun deleteUser(id: Int){
        safeCall(
            action = { repository.deleteUser(id) },
            onSuccess = {_message.value = it.message }
        )
    }

}