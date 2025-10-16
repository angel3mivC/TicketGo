package mx.tec.ticketgo.ui.screens.filters

data class FilterSection(
    val title: String,
    val options: Map<Int,String>,
    val selectedOptions: Set<String>
)