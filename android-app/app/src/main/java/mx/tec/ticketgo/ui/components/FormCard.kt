package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormCard(
    content: @Composable ColumnScope.() -> Unit
){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun FormAction(
    buttonText: String,
    isLoading: Boolean,
    fieldError: Boolean,
    serverError: Boolean,
    message: String?,
    onCreate: () -> Unit
    ){
    when {
        fieldError -> ErrorMessage("Completa los campos")
        serverError -> ErrorMessage(message ?: "Unknown error")
    }

    Spacer(modifier = Modifier.height((24.dp)))
    PrimaryButton(
        buttonText,
        Modifier.fillMaxWidth()
    ) { onCreate() }

    if (isLoading) CircularProgressIndicator()
}