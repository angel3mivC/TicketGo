package mx.tec.ticketgo.ui.screens.Home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketScreen
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable


fun tecnicoHomeScreen(ticketViewModel: TicketsViewModel){
    TecnicoTicketScreen(ticketViewModel)
}
@Composable
fun mesaHomeScreen(){
    Text(
        "Inicio mesa"
    )
}


@Composable
fun adminHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    AdminTicketScreen(commentViewModel, ticketViewModel, navController)
}