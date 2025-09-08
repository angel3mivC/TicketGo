package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.errorTextFieldColor
import mx.tec.ticketgo.ui.theme.textFieldColor

@Composable
fun InputTextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    hint: String,
    singleLine: Boolean = true,
    error: Boolean = false,
    keyboard: KeyboardType
){
    OutlinedTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(hint) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null) } },
        singleLine = singleLine,
        visualTransformation = if(keyboard == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        shape = RoundedCornerShape(18.dp),
        isError = error,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedLabelColor = MaterialTheme.colorScheme.textFieldColor,
            unfocusedLabelColor = MaterialTheme.colorScheme.textFieldColor,
            focusedIndicatorColor = MaterialTheme.colorScheme.textFieldColor,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.textFieldColor,
            focusedTextColor = MaterialTheme.colorScheme.textFieldColor,
            unfocusedTextColor = MaterialTheme.colorScheme.textFieldColor,
            errorTextColor = MaterialTheme.colorScheme.errorTextFieldColor,
            errorLabelColor = MaterialTheme.colorScheme.errorTextFieldColor,
            errorIndicatorColor = MaterialTheme.colorScheme.errorTextFieldColor,
            errorContainerColor = MaterialTheme.colorScheme.background
        )
    )
}