package mx.tec.ticketgo.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.components.FormAction
import mx.tec.ticketgo.ui.components.FormCard
import mx.tec.ticketgo.ui.components.InputTextField
import mx.tec.ticketgo.ui.components.Spinner
import mx.tec.ticketgo.ui.components.TopBar
import mx.tec.ticketgo.ui.viewmodels.CommentsViewModel
import mx.tec.ticketgo.ui.viewmodels.TicketsViewModel

@Composable
fun TicketFormScreen(
    commentsViewModel: CommentsViewModel, 
    ticketsViewModel: TicketsViewModel,
    navController: NavController
){
    val isLoading by ticketsViewModel.isLoading.collectAsStateWithLifecycle()
    val message by ticketsViewModel.message.collectAsStateWithLifecycle()
    val serverError by ticketsViewModel.error.collectAsStateWithLifecycle()
    val ticketId by ticketsViewModel.ticketId.collectAsStateWithLifecycle()
    val fieldError = remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priorityId by remember { mutableIntStateOf(-1) }
    var categoryId by remember { mutableIntStateOf(-1) }
    var comment by remember {mutableStateOf("")}

    // Resetear campos cuando se entra al formulario
    LaunchedEffect(Unit) {
        title = ""
        description = ""
        priorityId = -1
        categoryId = -1
        comment = ""
        fieldError.value = false
        ticketsViewModel.resetTicketId()
    }

    val priorityOptions = mapOf(
        "Alta" to 1,
        "Media" to 2,
        "Baja" to 3
    )
    val categoryOptions = mapOf(
        "En proceso" to 1,
        "Garantia" to 2,
        "Daño inducido" to 3
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        // TopBar para navegación
        TopBar(
            title = "Crear Ticket",
            onBack = { navController.navigateUp() }
        )
        
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
                    hint = "Título",
                    error = fieldError.value
                )

                InputTextField(
                    value = description,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { description = it },
                    hint = "Descripción",
                    error = fieldError.value
                )

                Spinner(
                    hint = "Prioridad",
                    options = priorityOptions,
                    error = fieldError.value
                ) { priorityId = it }

                Spinner(
                    hint = "Categoria",
                    options = categoryOptions,
                    error = fieldError.value
                ) { categoryId = it }

                InputTextField(
                    value = comment,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { comment = it },
                    hint = "Comentarios*"
                )
            }

            FormAction(
                "Crear",
                isLoading,
                fieldError.value,
                serverError,
                message
            ) {
                if (title.isBlank() || description.isBlank() || categoryId == -1 || priorityId == -1) {
                    fieldError.value = true
                } else {
                    fieldError.value = false
                    ticketsViewModel.createTicket(title, description, categoryId, priorityId)
                }
            }
        }
    }

    // Navegar de vuelta después de crear el ticket exitosamente
    LaunchedEffect(ticketId) {
        if (ticketId != null && ticketId!! > 0) {
            if (comment.isNotBlank()) {
                commentsViewModel.createComment(ticketId!!, comment)
            }
            // Navegar de vuelta a mesaHome después de un pequeño delay
            kotlinx.coroutines.delay(1000)
            navController.navigate("mesaHome") {
                popUpTo("mesaHome") { inclusive = false }
            }
            // Resetear el ticketId para evitar navegación automática en futuras visitas
            ticketsViewModel.resetTicketId()
        }
    }
}