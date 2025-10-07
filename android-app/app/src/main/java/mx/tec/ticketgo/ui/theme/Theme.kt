package mx.tec.ticketgo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    tertiary = Tertiary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface
)

@Suppress("UnusedReceiverParameter")
val ColorScheme.textFieldColor: Color get() = Color(0xFF808080)
@Suppress("UnusedReceiverParameter")
val ColorScheme.errorTextFieldColor: Color get() = Color(0xFFFF4A4A)
@Suppress("UnusedReceiverParameter")
val ColorScheme.alarmColor: Color get() = Color(0xFFFFAEAC)
@Suppress("UnusedReceiverParameter")
val ColorScheme.alarmMessageColor: Color get() = Color(0xFFE10600)
@Suppress("UnusedReceiverParameter")
val ColorScheme.ActiveElement: Color get() = Color(0XFFFF0000)
@Suppress("UnusedReceiverParameter")
val ColorScheme.UnactiveElement: Color get() = Color.Black
@Suppress("UnusedReceiverParameter")
val ColorScheme.EmptyElement: Color get() = Color(0xFFD9D9D9)

@Suppress("UnusedReceiverParameter")
val ColorScheme.ChipsGreen: Color get() = Color(0xFF61C800)
@Suppress("UnusedReceiverParameter")
val ColorScheme.ChipsYellow: Color get() = Color(0xFFFFAE00)
@Suppress("UnusedReceiverParameter")
val ColorScheme.ChipsBlue: Color get() = Color(0xFF0080FF)
@Suppress("UnusedReceiverParameter")
val ColorScheme.ChipsRed: Color get() = Color(0xFFFF3B3E)
@Suppress("UnusedReceiverParameter")
val ColorScheme.ChipsGray: Color get() = Color(0xFF8B8B8B)


@Composable
fun TicketGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}