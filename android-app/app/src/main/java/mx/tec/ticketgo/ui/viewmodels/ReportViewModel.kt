package mx.tec.ticketgo.ui.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.tec.ticketgo.data.models.TicketReportByCategory
import mx.tec.ticketgo.data.models.TicketReportByDate
import mx.tec.ticketgo.data.models.TicketReportByDateRequest
import mx.tec.ticketgo.data.models.TicketReportByPriority
import mx.tec.ticketgo.data.models.TicketReportByState
import mx.tec.ticketgo.data.models.TicketReportByTechnician
import mx.tec.ticketgo.data.repository.ReportRepository

class ReportViewModel(private val repository: ReportRepository): BaseViewModel() {

    private val _reportsByState = MutableStateFlow<List<TicketReportByState>>(emptyList())
    val reportsByState: StateFlow<List<TicketReportByState?>> = _reportsByState

    private val _reportsByPriority = MutableStateFlow<List<TicketReportByPriority>>(emptyList())
    val reportsByPriority: StateFlow<List<TicketReportByPriority>> = _reportsByPriority

    private val _reportsByCategory = MutableStateFlow<List<TicketReportByCategory>>(emptyList())
    val reportsByCategory: StateFlow<List<TicketReportByCategory>> = _reportsByCategory

    private val _reportsByTechnician = MutableStateFlow<List<TicketReportByTechnician>>(emptyList())
    val reportsByTechnician: StateFlow<List<TicketReportByTechnician>> = _reportsByTechnician

    private val _reportsByDate = MutableStateFlow<List<TicketReportByDate>>(emptyList())
    val reportsByDate: StateFlow<List<TicketReportByDate>> = _reportsByDate

    fun reportByState(){
        safeCall(
            action = { repository.reportByState() },
            onSuccess = { _reportsByState.value = it }
        )
    }

    fun reportByPriority(){
        safeCall(
            action = { repository.reportByPriority() },
            onSuccess = { _reportsByPriority.value = it}
        )
    }

    fun reportByCategory(){
        safeCall(
            action = { repository.reportByCategory() },
            onSuccess = { _reportsByCategory.value = it}
        )
    }

    fun reportByTechnician(){
        safeCall(
            action = { repository.reportByTechnician() },
            onSuccess = { _reportsByTechnician.value = it }
        )
    }

    fun reportByData(startDate: String?, endDate: String?){
        val request = TicketReportByDateRequest(startDate, endDate)
        safeCall(
            action = { repository.reportByData(request) },
            onSuccess = { _reportsByDate.value = it }
        )
    }
}