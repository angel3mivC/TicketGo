package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.Summary
import mx.tec.ticketgo.data.repository.DashboardRepository

class DashboardViewModel(private val repository: DashboardRepository): BaseViewModel() {

    private val _summary = MutableStateFlow<Summary?>(null)
    val summary: StateFlow<Summary?> = _summary

    private val _totalTickets = MutableStateFlow<Int?>(null)
    val totalTicketsViewModel: StateFlow<Int?> = _totalTickets

    private val _openTickets = MutableStateFlow<Int?>(null)
    val openTicketsViewModel: StateFlow<Int?> = _openTickets

    private val _closedTickets = MutableStateFlow<Int?>(null)
    val closedTickets: StateFlow<Int?> = _closedTickets

    private val _myTickets = MutableStateFlow<Int?>(null)
    val myTickets: StateFlow<Int?> = _myTickets

    fun dashboardSummary(){
        safeCall(
            action = { repository.dashboardSummary() },
            onSuccess = { _summary.value = it }
        )
    }

    fun totalTickets(){
        safeCall(
            action = { repository.totalTickets() },
            onSuccess = { _totalTickets.value = it.total }
        )
    }

    fun openTickets(){
        safeCall(
            action = { repository.openTickets() },
            onSuccess = { _openTickets.value = it.abiertos }

        )
    }

    fun closedTickets(){
        safeCall(
            action = { repository.closedTickets() },
            onSuccess = { _closedTickets.value = it.cerrados }
        )
    }

    fun myTickets(){
        safeCall(
            action = { repository.myTickets() },
            onSuccess = { _myTickets.value = it.mis_tickets }
        )
    }
}