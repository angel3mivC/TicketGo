package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.CategoryResponse
import mx.tec.ticketgo.data.models.PriorityResponse
import mx.tec.ticketgo.data.models.RolesResponse
import mx.tec.ticketgo.data.models.StatesResponse
import mx.tec.ticketgo.data.repository.CatalogRepository

class CatalogViewModel(private val repository: CatalogRepository): BaseViewModel() {

    private val _roles = MutableStateFlow<List<RolesResponse>>(emptyList())
    val roles: StateFlow<List<RolesResponse>> = _roles

    private val _states = MutableStateFlow<List<StatesResponse>>(emptyList())
    val states: StateFlow<List<StatesResponse>> = _states

    private val _categories = MutableStateFlow<List<CategoryResponse>>(emptyList())
    val categories: StateFlow<List<CategoryResponse>> = _categories

    private val _priorities = MutableStateFlow<List<PriorityResponse>>(emptyList())
    val priorities: StateFlow<List<PriorityResponse>> = _priorities

    fun getRoles(){
        safeCall(
            action = { repository.getRoles() },
            onSuccess = { _roles.value = it }
        )
    }

    fun getStates(){
        safeCall(
            action = { repository.getStates() },
            onSuccess = { _states.value = it }
        )
    }

    fun getCategories(){
        safeCall(
            action = { repository.getCategories() },
            onSuccess = { _categories.value = it }
        )
    }

    fun getPriorities(){
        safeCall(
            action = { repository.getPriorities() },
            onSuccess = { _priorities.value = it }
        )
    }
}