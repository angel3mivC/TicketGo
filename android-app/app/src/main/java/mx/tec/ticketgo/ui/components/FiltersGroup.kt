package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterGroup(title: String, options:  Map<Int, String>, selectedOptions: Set<String>, onOptionSelected: (String, Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()){
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            options.forEach { (_, optionText) ->
                val isSelected = selectedOptions.contains(optionText)
                FilterChip(
                    selected = isSelected,
                    onClick = { onOptionSelected(optionText, !isSelected) },
                    label = {
                        Text(
                            text = optionText,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFE0E0E0),
                        selectedContainerColor = Color.Black,
                        labelColor = Color.Black,
                        selectedLabelColor = Color.White,
                    ),
                    border = BorderStroke(0.dp, Color.Transparent)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

