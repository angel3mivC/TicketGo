package mx.tec.ticketgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.ticketgo.data.repository.AuthRepository
import mx.tec.ticketgo.ui.theme.TicketGoTheme
import mx.tec.ticketgo.ui.screens.login.LoginScreen
import mx.tec.ticketgo.ui.screens.login.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketGoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val loginViewModel: LoginViewModel = viewModel()
                    LoginScreen(viewModel = loginViewModel)

                }
            }
        }
    }
}