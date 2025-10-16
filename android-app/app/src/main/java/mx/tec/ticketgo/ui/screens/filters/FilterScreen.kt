package mx.tec.ticketgo.ui.screens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ticketgo.ui.components.FilterGroup

@Composable
fun FilterScreen ( sections: List<FilterSection>, onOptionSelected: (String, String, Boolean) -> Unit, onClose: () -> Unit) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "Filtros",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar filtros"
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            sections.forEach { section ->
                FilterGroup(
                    title = section.title,
                    options = section.options,
                    selectedOptions = section.selectedOptions,
                    onOptionSelected = { option, isSelected ->
                        onOptionSelected(section.title, option, isSelected)
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

        }
}

/*
@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    val sampleSections = listOf(
        FilterSection(
            title = "Estado",
            options = mapOf(1 to "Abierto", 2 to "En progreso", 3 to "Finalizado"),
            selectedOptions = setOf("Abierto")
        ),
        FilterSection(
            title = "Categoría",
            options = mapOf(1 to "En proceso", 2 to "Daño inducido", 3 to "Garantia"),
            selectedOptions = emptySet()
        )
    )

    FilterScreen(
        sections = sampleSections,
        onOptionSelected = { _, _, _ -> },
        onClose = {}
    )
}
*/