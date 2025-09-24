package mx.tec.ticketgo.ui.screens.MainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.NavBar
import mx.tec.ticketgo.ui.screens.History.HistoryScreen
import mx.tec.ticketgo.ui.screens.Home.HomeScreen
import mx.tec.ticketgo.ui.screens.Ticket.TicketFormScreen

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {NavBar(navController,Triple("formulario", "Formulario", Icons.Default.Home))}
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") { HomeScreen() }
            composable("formulario") { TicketFormScreen() }
            composable("historial") { HistoryScreen() }
        }
    }
}