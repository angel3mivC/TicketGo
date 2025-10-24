package mx.tec.ticketgo.ui.screens.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import mx.tec.ticketgo.R

@Composable
fun SplashScreen(navController: NavController) {
    val redColor = Color(0xFFE30613) // Color rojo MAC
    val view = LocalView.current
    
    // Configurar la barra de estado con el color rojo
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = redColor.toArgb()
        window.navigationBarColor = redColor.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
    
    // Efecto para navegar después de 2 segundos y restaurar colores
    LaunchedEffect(Unit) {
        delay(2000) // 2 segundos
        
        // Restaurar los colores de las barras antes de navegar
        val window = (view.context as Activity).window
        window.statusBarColor = Color.White.toArgb()
        window.navigationBarColor = Color.White.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // Pantalla roja con logo centrado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(redColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_mac),
            contentDescription = "Logo MAC",
            modifier = Modifier.size(138.dp)
        )
    }
}

