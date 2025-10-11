package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mx.tec.ticketgo.ui.components.FormAction
import mx.tec.ticketgo.ui.components.FormCard
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.components.Spinner
import mx.tec.ticketgo.ui.viewmodels.UserViewModel

@Composable
fun UserForm(
    viewModel: UserViewModel,
    buttonText: String,
    onSubmit: (name: String, email: String, password: String, roleId: Int) -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val serverError by viewModel.error.collectAsStateWithLifecycle()
    val fieldError = remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var roleId by remember { mutableIntStateOf(-1) }

    val roleOptions = mapOf(
        "Administrador" to 1,
        "Mesa" to 2,
        "Técnico" to 3
    )

    FormCard {
        InputTextField(
            value = name,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { name = it },
            hint = "Nombre",
            error = fieldError.value
        )

        InputTextField(
            value = email,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { email = it },
            hint = "Correo",
            error = fieldError.value
        )

        InputTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { password = it },
            hint = "Contraseña",
            error = fieldError.value
        )

        Spinner(
            hint = "Rol",
            options = roleOptions,
            error = fieldError.value
        ) { roleId = it }
    }

    FormAction(
        buttonText,
        isLoading,
        fieldError.value,
        serverError,
        message
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || roleId == -1) {
            fieldError.value = true
        } else {
            fieldError.value = false
            onSubmit(name, email, password, roleId)
        }
    }
}