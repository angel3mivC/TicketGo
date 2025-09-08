package mx.tec.ticketgo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AppText(text: String, modifier: Modifier = Modifier, fontSize: TextUnit, fontWeight: FontWeight, color: Color){
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}
@Composable
fun Title(text: String, modifier: Modifier = Modifier){
    AppText(text, modifier, 24.sp, FontWeight.SemiBold, MaterialTheme.colorScheme.onBackground)
}

@Composable
fun Subtitle(text: String, modifier: Modifier = Modifier){
    AppText(text, modifier, 20.sp, FontWeight.SemiBold, MaterialTheme.colorScheme.onBackground)
}

@Composable
fun Annoucement(text: String, modifier: Modifier = Modifier){
    AppText(text, modifier, 35.sp, FontWeight.Bold, MaterialTheme.colorScheme.onBackground)
}

@Composable
fun BodyText(text: String, modifier: Modifier = Modifier){
    AppText(text, modifier, 12.sp, FontWeight.Medium, MaterialTheme.colorScheme.onBackground)
}