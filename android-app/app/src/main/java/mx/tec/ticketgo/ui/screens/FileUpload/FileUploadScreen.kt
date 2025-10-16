package mx.tec.ticketgo.ui.screens.FileUpload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import mx.tec.ticketgo.ui.components.AgregarArchivos
import mx.tec.ticketgo.ui.components.NewFileItem
import mx.tec.ticketgo.ui.viewmodels.FileViewModel

@Composable
fun FileUploadScreen(
    ticketId: Int,
    fileViewModel: FileViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val files by fileViewModel.files.collectAsStateWithLifecycle()
    val isUploading by fileViewModel.isUploading.collectAsStateWithLifecycle()
    val uploadMessage by fileViewModel.uploadMessage.collectAsStateWithLifecycle()
    
    // Limpiar mensaje cuando se monta el componente
    LaunchedEffect(Unit) {
        fileViewModel.clearMessage()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Zona de agregar archivos
        AgregarArchivos(
            onFileSelected = { uri ->
                fileViewModel.addFile(context, uri, ticketId)
            }
        )
        
        // Lista de archivos seleccionados
        if (files.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                files.forEach { file ->
                    NewFileItem(
                        fileName = file.name,
                        fileType = file.type,
                        onRemove = {
                            fileViewModel.removeFile(file.id)
                        }
                    )
                }
            }
        }
        
        // Mensaje de estado
        uploadMessage?.let { message ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.contains("Error") || message.contains("excede")) {
                        Color(0xFFFFEBEE)
                    } else {
                        Color(0xFFE8F5E8)
                    }
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = if (message.contains("Error") || message.contains("excede")) {
                        Color(0xFFD32F2F)
                    } else {
                        Color(0xFF2E7D32)
                    }
                )
            }
        }
        
        // Espaciador para empujar el botón hacia abajo
        Spacer(modifier = Modifier.weight(1f))
        
        // Botón de cargar evidencia
        Button(
            onClick = {
                fileViewModel.uploadFiles(ticketId)
            },
            enabled = files.isNotEmpty() && !isUploading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Cargar evidencia",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
