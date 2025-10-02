package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.errorTextFieldColor
import mx.tec.ticketgo.ui.theme.textFieldColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spinner(error: Boolean = false, selectedOption: String, hint: String, options: List<String>, onSelection: (String) -> Unit){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedOption,
            label = { Text(hint) },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelection(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.textFieldColor
                    )
                )
            }
        }
    }
}