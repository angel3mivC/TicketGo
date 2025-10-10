package mx.tec.ticketgo.ui.screens.MainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.NavBar
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.screens.Home.adminHomeScreen
import mx.tec.ticketgo.ui.screens.Home.mesaHomeScreen
import mx.tec.ticketgo.ui.screens.Home.tecnicoHomeScreen
import mx.tec.ticketgo.ui.screens.forms.TicketFormScreen
import mx.tec.ticketgo.ui.screens.gallery.GalleryScreen
import mx.tec.ticketgo.ui.screens.login.LoginScreen
import mx.tec.ticketgo.ui.viewmodels.LoginViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val ticketViewModel: TicketsViewModel = viewModel()

    val loginViewModel: LoginViewModel = viewModel()

    Scaffold(
        topBar = {
            when(currentRoute){
                "gallery" -> TopBar("Galeria", navController)
                "historial" -> TopBar("Crear ticket", navController)
            }
        },
        bottomBar = {
            if (currentRoute in listOf("mesaHome", "adminHome")) {
                when (currentRoute) {
                    "mesaHome" -> NavBar(navController, currentRoute, Icons.Default.Add, "TicketFormScreen")
                    "adminHome" -> NavBar(navController, currentRoute, Icons.Default.Add, "Gallery")
                }
            }
        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
        composable("inicio") { LoginScreen(loginViewModel) }
        composable("gallery") { GalleryScreen() }
        composable("historial") { TicketFormScreen(ticketViewModel) }
        composable("tecnicoHome") { tecnicoHomeScreen() }
        composable("adminHome") { adminHomeScreen() }
        composable("mesaHome") { mesaHomeScreen() }
    }

    }
}
