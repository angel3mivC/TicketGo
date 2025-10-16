package mx.tec.ticketgo.ui.screens.Home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketScreen
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

val homeFilters = listOf(
    FilterSection(
        "Estado",
        mapOf(1 to "Abierto", 2 to "En progreso", 3 to "Cerrado", 4 to "Resuelto", 5 to "Reabierto"),
        setOf("Abierto")
    ),
    FilterSection(
        "Categoría",
        mapOf(1 to "En Proceso", 2 to "Daño Inducido", 3 to "Garantía"),
        emptySet()
    )
)

@Composable


fun tecnicoHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    TecnicoTicketScreen(commentViewModel, ticketViewModel, navController)
}
@Composable
fun mesaHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    AdminTicketScreen(commentViewModel, ticketViewModel, navController, false)
}


@Composable
fun adminHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    AdminTicketScreen(commentViewModel, ticketViewModel, navController)
}