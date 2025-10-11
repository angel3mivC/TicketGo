package mx.tec.ticketgo.ui.screens.login

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.components.Annoucement
import mx.tec.ticketgo.ui.components.ErrorMessage
import mx.tec.ticketgo.ui.components.PrimaryButton
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, context: Context, navController: NavController){
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val message by viewModel.message.collectAsState()

    var email by remember { mutableStateOf("") }
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
            value = email,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {email = it},
            hint = "Email",
            error = error
        )

        InputTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {password = it},
            hint = "Contraseña",
            keyboard = KeyboardType.Password,
            error = error
        )

        Spacer(modifier = Modifier.height(40.dp))

        message?.let {
            if(error) {
                ErrorMessage(it)
            }else{
                val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val userId = sharedPref.getInt("id_usuario",-1)
                when (userId) {
                    1 -> navController.navigate("adminHome")
                    2 -> navController.navigate("mesaHome")
                    3 -> navController.navigate("tecnicoHome")
                    else -> navController.navigate("inicio")
                }
            }
        }

        PrimaryButton(
            "Iniciar sesión",
            Modifier.fillMaxWidth(),
        ){ viewModel.login(email, password, context) }

        if (isLoading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator()
        }
    }
}