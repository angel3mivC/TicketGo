package mx.tec.ticketgo.ui.screens.Home

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketScreen
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable


fun tecnicoHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    TecnicoTicketScreen(commentViewModel, ticketViewModel, navController)
}
@Composable
fun mesaHomeScreen(){
    Text(
        "Inicio mesa"
    )
}


@Composable
fun adminHomeScreen(commentViewModel: CommentsViewModel, ticketViewModel: TicketsViewModel, navController: NavController){
    AdminTicketScreen(commentViewModel, ticketViewModel, navController, false)
}

@Composable
fun usuarioHome(){
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val rolId = sharedPref.getInt("id_role",-1)
    val userId = sharedPref.getInt("id_user",-1)
    Column {
        Text(rolId.toString())
        Text(userId.toString())
    }

}