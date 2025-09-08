package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.textFieldColor

@Composable
fun GenericButton(text: String, modifier: Modifier, color: Color, textColor: Color, onClick: () -> Unit){
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(text, color = textColor)
    }
}

@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit){
    GenericButton(text, modifier, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary, onClick)
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit){
    GenericButton(text, modifier, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary, onClick)
}

@Composable
fun TertiaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit){
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClick = onClick
            ),
        textAlign = TextAlign.End,
        color = MaterialTheme.colorScheme.textFieldColor,
        style = TextStyle(textDecoration = TextDecoration.Underline)
    )
}