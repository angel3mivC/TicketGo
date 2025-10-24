package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onSubmit: (name: String, email: String, password: String, roleId: Int) -> Unit,
    initialData: mx.tec.ticketgo.data.models.GetUserResponse? = null
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val serverError by viewModel.error.collectAsStateWithLifecycle()
    val fieldError = remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(initialData?.nombre ?: "") }
    var email by remember { mutableStateOf(initialData?.correo ?: "") }
    var password by remember { mutableStateOf("") }
    var roleId by remember { mutableIntStateOf(initialData?.id_rol ?: -1) }

    // Cargar datos iniciales cuando se proporcionan
    LaunchedEffect(initialData) {
        if (initialData != null) {
            name = initialData.nombre
            email = initialData.correo
            roleId = initialData.id_rol
            password = "" // No cargar contraseña por seguridad
        }
    }

    val roleOptions = mapOf(
        "Administrador" to 1,
        "Mesa" to 2,
        "Técnico" to 3
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Spacer(modifier = Modifier.height(16.dp))

        FormAction(
            buttonText,
            isLoading,
            fieldError.value,
            serverError,
            message
        ) {
            val isEditing = initialData != null
            val passwordRequired = !isEditing || password.isNotBlank()
            
            if (name.isBlank() || email.isBlank() || (passwordRequired && password.isBlank()) || roleId == -1) {
                fieldError.value = true
            } else {
                fieldError.value = false
                onSubmit(name, email, password, roleId)
            }
        }
    }
}