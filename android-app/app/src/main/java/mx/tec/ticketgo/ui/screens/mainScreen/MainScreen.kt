package mx.tec.ticketgo.ui.screens.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.components.NavBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
//import mx.tec.ticketgo.ui.screens.history.TicketHistoryScreen
import mx.tec.ticketgo.ui.screens.Home.adminHomeScreen
import mx.tec.ticketgo.ui.screens.Home.mesaHomeScreen
import mx.tec.ticketgo.ui.screens.Home.tecnicoHomeScreen
import mx.tec.ticketgo.ui.screens.Home.usuarioHome
import mx.tec.ticketgo.ui.screens.forms.CreateUserScreen
import mx.tec.ticketgo.ui.screens.forms.TicketFormScreen
import mx.tec.ticketgo.ui.screens.gallery.GalleryScreen
import mx.tec.ticketgo.ui.screens.login.LoginScreen
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketDetailScreen
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketDetailScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketScreen
import mx.tec.ticketgo.ui.screens.FileUpload.FileUploadScreen
import mx.tec.ticketgo.ui.screens.Ticket.AdminTicketDetailHistorialScreen
import mx.tec.ticketgo.ui.screens.Ticket.TecnicoTicketDetailHistorialScreen
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.EvidenceViewModel
import mx.tec.ticketgo.ui.viewmodels.FileViewModel
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
    val fileViewModel: FileViewModel = viewModel()
    val evidenceViewModel: EvidenceViewModel = viewModel()

    val loginViewModel: LoginViewModel = viewModel()
    
    // Conectar FileViewModel con EvidenceViewModel
    LaunchedEffect(Unit) {
        fileViewModel.setEvidenceViewModel(evidenceViewModel)
    }

    Scaffold(
        topBar = {
            when(currentRoute){
                "gallery" -> TopBar("Galeria", navController)
                "historial" -> TopBar("Crear ticket", navController)
                "ticketDetail" -> TopBar("Detalle del ticket", navController)
                else -> {
                    when {
                        currentRoute?.startsWith("fileUpload/") == true -> TopBar("Carga de Archivos", navController)
                        currentRoute?.startsWith("gallery/") == true -> TopBar("Galeria", navController)
                        currentRoute?.startsWith("tecnicoTicketDetail/") == true || 
                        currentRoute?.startsWith("adminTicketDetail/") == true -> TopBar("Detalle del ticket", navController)
                        else -> null
                    }
                }
            }
        },
        bottomBar = {
            // Mostrar NavBar en homes y pantallas de historial
            when (currentRoute) {
                "adminHome" -> NavBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    middleBottomIcon = Icons.Default.PersonAdd,
                    middleBottomRoute = "createUserForm",
                    historialRoute = "adminTicketHistory",
                    homeRoute = "adminHome"
                )
                "mesaHome" -> NavBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    middleBottomIcon = Icons.Default.Add,
                    middleBottomRoute = "TicketForm",
                    historialRoute = "adminTicketHistory",
                    homeRoute = "mesaHome"
                )
                "adminTicketHistory" -> NavBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    middleBottomIcon = Icons.Default.PersonAdd,
                    middleBottomRoute = "createUserForm",
                    historialRoute = "adminTicketHistory",
                    homeRoute = "adminHome"
                )
                else -> null
            }
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
            composable("gallery") { GalleryScreen(0, evidenceViewModel) }
            composable("usuarioHome") { usuarioHome() }

            //Tecnico
            composable("tecnicoHome") { tecnicoHomeScreen(commentsViewModel,ticketsViewModel, navController) }
            composable("tecnicoTicketDetail/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                TecnicoTicketDetailScreen(ticketId, commentsViewModel, ticketsViewModel, fileViewModel, navController)
            }
            composable("tecnicoTicketHistory") { TecnicoTicketScreen(commentsViewModel, ticketsViewModel, navController, true) }
            composable("tecnicoTicketDetailHistorial/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                TecnicoTicketDetailHistorialScreen(ticketId, commentsViewModel, ticketsViewModel, navController)
            }

            //Mesa
            composable("mesaHome") { mesaHomeScreen(commentsViewModel, ticketsViewModel, navController) }
            composable ("TicketForm") { TicketFormScreen(commentsViewModel, ticketsViewModel) }

            //Admin
            composable("adminHome") { adminHomeScreen(commentsViewModel,ticketsViewModel, navController) }
            composable("adminTicketDetail/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                AdminTicketDetailScreen(ticketId, commentsViewModel, ticketsViewModel, navController)
            }
            composable("adminTicketHistory") { AdminTicketScreen(commentsViewModel, ticketsViewModel, navController, true) }
            composable("adminTicketDetailHistorial/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                AdminTicketDetailHistorialScreen(ticketId, commentsViewModel, ticketsViewModel, navController)
            }
            composable("createUserForm") { CreateUserScreen(userViewModel) }
            
            // File Upload
            composable("fileUpload/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                FileUploadScreen(ticketId, fileViewModel, navController)
            }
            
            // Gallery
            composable("gallery/{ticketId}") { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
                GalleryScreen(ticketId, evidenceViewModel)
            }
            //composable("editUserForm") { EditUserScreen(userId, userViewModel) }
        }
    }
}
