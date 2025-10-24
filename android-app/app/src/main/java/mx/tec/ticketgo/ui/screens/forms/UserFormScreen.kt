package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun CreateUserScreen(viewModel: UserViewModel, navController: NavController){
    val message by viewModel.message.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    // Limpiar mensaje cuando se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.clearMessage()
    }
    
    // Navegar de vuelta cuando se crea exitosamente (cuando termina de cargar y no hay error)
    LaunchedEffect(isLoading, error, message) {
        if (!isLoading && !error && message != null) {
            // Recargar usuarios antes de regresar
            viewModel.getUsers()
            navController.popBackStack()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            title = "Crear Usuario",
            navController = navController
        )
        
        UserForm(
            viewModel = viewModel, 
            buttonText = "Crear",
            onSubmit = { name, email, password, roleId ->
                viewModel.createUser(name, email, password, roleId)
            }
        )
    }
}

@Composable
fun EditUserScreen(userId: Int, viewModel: UserViewModel, navController: NavController) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    // Limpiar mensaje cuando se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.clearMessage()
    }
    
    // Cargar datos del usuario cuando se entra a la pantalla
    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }
    
    // Navegar de vuelta cuando se actualiza exitosamente (cuando termina de cargar y no hay error)
    LaunchedEffect(isLoading, error, message) {
        if (!isLoading && !error && message != null) {
            // Recargar usuarios antes de regresar
            viewModel.getUsers()
            navController.popBackStack()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
            title = "Editar Usuario",
            navController = navController
        )
        
        UserForm(
            viewModel = viewModel, 
            buttonText = "Guardar cambios",
            initialData = user,
            onSubmit = { name, email, password, roleId ->
                viewModel.updateUser(userId, name, email, password, roleId)
            }
        )
    }
}
