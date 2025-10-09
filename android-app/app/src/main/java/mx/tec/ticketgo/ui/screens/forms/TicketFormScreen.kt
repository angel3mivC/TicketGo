package mx.tec.ticketgo.ui.screens.forms

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.components.Spinner
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun TicketFormScreen(viewModel: TicketsViewModel){
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var status by remember {mutableStateOf("")}
    var comments by remember {mutableStateOf("")}

    val priorityOptions = listOf("Alta", "Media", "Baja")
    val categoryOptions = listOf("En proceso", "Daño incluido", "Garantía")
    val statusOptions = listOf("Abierto", "En proceso", "Resuelto", "Cerrado", "Reabierto")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormCard {
            InputTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { title = it },
                hint = "Título"
            )

            InputTextField(
                value = description,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { description = it },
                hint = "Descripción"
            )

            Spinner(
                selectedOption = priority,
                hint = "Prioridad",
                options = priorityOptions
            ) { priority = it }

            Spinner(
                selectedOption = category,
                hint = "Categoria",
                options = categoryOptions
            ) { category = it }

            Spinner(
                selectedOption = status,
                hint = "Status",
                options = statusOptions
            ) { status = it }

            InputTextField(
                value = comments,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { comments = it },
                hint = "Comentarios*"
            )
        }
/*
        FormAction(
            buttonText = "Crear",
            isLoading = viewModel.isLoading,
            onCreate = { viewModel.addTicket(title, description, priority, category, status, comments) },
            context = LocalContext.current
        )

        viewModel.ticketId?.let {
            Toast.makeText(context, "Nuevo ticket $it", Toast.LENGTH_SHORT).show()
        }*/
    }
}