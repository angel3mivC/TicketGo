package mx.tec.ticketgo.ui.screens.MainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.NavBar
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.screens.History.HistoryScreen
import mx.tec.ticketgo.ui.screens.Home.HomeScreen
import mx.tec.ticketgo.ui.screens.gallery.GalleryScreen

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            when(currentRoute){
                "gallery" -> TopBar("Galeria", navController)
            }
        },
        bottomBar = {NavBar(navController, currentRoute, Icons.Default.Add, "gallery")}
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") { HomeScreen() }
            composable("gallery") { GalleryScreen() }
            composable("historial") { HistoryScreen() }
        }
    }
}