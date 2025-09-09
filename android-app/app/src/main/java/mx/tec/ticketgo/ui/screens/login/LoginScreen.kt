package mx.tec.ticketgo.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.components.Annoucement
import mx.tec.ticketgo.ui.components.ErrorMessage

import mx.tec.ticketgo.ui.components.PrimaryButton
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.components.TertiaryButton

@Composable
fun LoginScreen(viewModel: LoginViewModel){
    val context = LocalContext.current
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Annoucement("¡Bienvenido!")
        Text("Inicia sesión en tu cuenta")

        Spacer(modifier = Modifier.height(40.dp))

        InputTextField(
            value = user,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {user = it},
            hint = "Usuario",
            keyboard = KeyboardType.Text,
            error = viewModel.errorMessage != null
        )

        InputTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {password = it},
            hint = "Contraseña",
            keyboard = KeyboardType.Password,
            error = viewModel.errorMessage != null
        )

        TertiaryButton(
            "¿Olvidaste la contraseña?",
            modifier = Modifier.padding(top = 10.dp),
            ({
                Toast.makeText(context, "Recuperar contraseña", Toast.LENGTH_SHORT).show()
            })
        )

        Spacer(modifier = Modifier.height(40.dp))
        viewModel.errorMessage?.let {
            ErrorMessage(it)
            Spacer(modifier = Modifier.height(40.dp))
        }

        PrimaryButton(
            "Iniciar sesión",
            Modifier.fillMaxWidth(),
            {
                viewModel.login(user, password)
            }
        )

        if (viewModel.isLoading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator()
        }

        viewModel.token?.let {
            Toast.makeText(context, "Login OK. Token: $it", Toast.LENGTH_SHORT).show()
        }
    }
}