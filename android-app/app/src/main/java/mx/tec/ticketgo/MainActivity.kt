package mx.tec.ticketgo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.tec.ticketgo.ui.screens.MainScreen.MainScreen
import mx.tec.ticketgo.ui.theme.TicketGoTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketGoTheme {
                MainScreen()
            }
        }
    }
}