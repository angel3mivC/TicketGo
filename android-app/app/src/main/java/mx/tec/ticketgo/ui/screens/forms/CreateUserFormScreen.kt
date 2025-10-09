package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.components.Spinner
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun CreateUserScreen(viewModel: UserViewModel){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val roleOptions = listOf("")

    FormCard {
        InputTextField(
            value = name,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { name = it },
            hint = "Nombre"
        )

        InputTextField(
            value = email,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { email = it },
            hint = "Correo"
        )

        InputTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { password = it },
            hint = "Contraseña"
        )

        Spinner(
            selectedOption = role,
            hint = "Rol",
            options = roleOptions
        ) { role = it }
    }
/*
    FormAction(
        buttonText = "Crear",
        isLoading = viewModel.isLoading,
        successMessage = viewModel.successMessage,
        onCreate = { viewModel.createUser(name, email, password, role) },
        context = LocalContext.current
    )*/
}