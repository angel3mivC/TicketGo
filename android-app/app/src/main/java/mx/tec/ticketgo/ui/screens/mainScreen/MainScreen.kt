package mx.tec.ticketgo.ui.screens.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.TopBar
//import mx.tec.ticketgo.ui.screens.history.TicketHistoryScreen
import mx.tec.ticketgo.ui.screens.Home.adminHomeScreen
import mx.tec.ticketgo.ui.screens.Home.mesaHomeScreen
import mx.tec.ticketgo.ui.screens.Home.tecnicoHomeScreen
import mx.tec.ticketgo.ui.screens.Home.usuarioHome
import mx.tec.ticketgo.ui.screens.forms.CreateUserScreen
import mx.tec.ticketgo.ui.screens.forms.TicketFormScreen
import mx.tec.ticketgo.ui.screens.gallery.GalleryScreen
//import mx.tec.ticketgo.ui.screens.history.TicketHistoryScreen
import mx.tec.ticketgo.ui.screens.login.LoginScreen
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.LoginViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val ticketsViewModel: TicketsViewModel = viewModel()
    val commentsViewModel: CommentsViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val loginViewModel: LoginViewModel = viewModel()

    Scaffold(
        topBar = {
            when(currentRoute){
                "gallery" -> TopBar("Galeria", navController)
                "historial" -> TopBar("Crear ticket", navController)
            }
        },
        bottomBar = {

        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {

            //Rutas comunes
            composable("login") { LoginScreen(loginViewModel, context, navController) }
            //composable("historial") { TicketHistoryScreen() }
            composable("gallery") { GalleryScreen() }
            composable("usuarioHome") { usuarioHome() }

            //Tecnico
            composable("tecnicoHome") { tecnicoHomeScreen(commentsViewModel,ticketsViewModel, navController) }


            //Mesa
            composable("mesaHome") { mesaHomeScreen() }
            composable ("TicketForm") { TicketFormScreen(commentsViewModel, ticketsViewModel) }


            //Admin
            composable("adminHome") { adminHomeScreen(commentsViewModel,ticketsViewModel, navController) }
            composable("createUserForm") { CreateUserScreen(userViewModel) }
            //composable("editUserForm") { EditUserScreen(userId, userViewModel) }
        }
    }
}
