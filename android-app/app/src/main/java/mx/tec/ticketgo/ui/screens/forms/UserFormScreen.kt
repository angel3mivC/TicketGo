package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.runtime.Composable
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun CreateUserScreen(viewModel: UserViewModel){
    UserForm(viewModel, "Crear") { name, email, password, roleId ->
            viewModel.createUser(name, email, password, roleId)
    }
}

@Composable
fun EditUserScreen(userId: Int, viewModel: UserViewModel) {
    UserForm(viewModel, "Guardar cambios") { name, email, password, roleId ->
        viewModel.updateUser(userId, name, email, password, roleId)
    }
}
