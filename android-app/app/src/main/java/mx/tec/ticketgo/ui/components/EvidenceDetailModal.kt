package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import mx.tec.ticketgo.data.models.Evidence
import mx.tec.ticketgo.utils.DateFormatter
import android.os.Build
import android.provider.MediaStore
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import android.app.DownloadManager
import android.os.Environment
import android.content.Context
import android.util.Log
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.tec.ticketgo.data.network.TokenStorage

private fun getMimeType(fileType: String): String {
    return when (fileType.uppercase()) {
        "PDF" -> "application/pdf"
        "JPG", "JPEG" -> "image/jpeg"
        "PNG" -> "image/png"
        "GIF" -> "image/gif"
        "WEBP" -> "image/webp"
        "DOC" -> "application/msword"
        "DOCX" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "TXT" -> "text/plain"
        else -> "application/octet-stream"
    }
}

@Composable
fun EvidenceDetailModal(
    evidence: Evidence,
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var downloadState by remember { mutableStateOf("idle") } // idle, downloading, completed
    
    suspend fun downloadFile() {
        Log.i("DOWNLOAD_DEBUG", "🚀 Iniciando descarga para: ${evidence.fileName}")
        Log.i("DOWNLOAD_DEBUG", "📁 FileUrl: ${evidence.fileUrl}")
        Log.i("DOWNLOAD_DEBUG", "📂 LocalPath: ${evidence.localPath}")
        Log.i("DOWNLOAD_DEBUG", "📊 Estado actual: $downloadState")
        
        if (downloadState == "downloading") {
            Log.i("DOWNLOAD_DEBUG", "⏸️ Ya está descargando, cancelando")
            return // Evitar múltiples descargas
        }
        
        Log.i("DOWNLOAD_DEBUG", "🔄 Cambiando estado a downloading")
        downloadState = "downloading"
        
        // Intentar descargar desde URL de la API primero
        evidence.fileUrl?.let { fileUrl ->
            Log.i("DOWNLOAD_DEBUG", "🌐 Intentando descarga desde URL: $fileUrl")
            if (fileUrl.isNotEmpty()) {
                try {
                    // Mover la operación de red al hilo de IO
                    val result = withContext(Dispatchers.IO) {
                        // Descargar desde URL
                        val url = URL(fileUrl)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        
                        // Agregar token de autorización
                        val token = runBlocking { TokenStorage.getToken() }
                        if (token != null) {
                            connection.setRequestProperty("Authorization", "Bearer $token")
                            Log.i("DOWNLOAD_DEBUG", "🔑 Token agregado a la descarga: Bearer $token")
                        } else {
                            Log.w("DOWNLOAD_DEBUG", "⚠️ No se encontró token de autorización")
                        }
                        
                        connection.connect()
                        
                        Log.i("DOWNLOAD_DEBUG", "📡 Código de respuesta: ${connection.responseCode}")
                        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                            val inputStream = connection.inputStream
                            var success = false
                            
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // Para Android 10+, usar MediaStore
                                val resolver = context.contentResolver
                                val contentValues = ContentValues().apply {
                                    put(MediaStore.MediaColumns.DISPLAY_NAME, evidence.fileName)
                                    put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(evidence.fileType))
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                                }
                                
                                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                                uri?.let { fileUri ->
                                    resolver.openOutputStream(fileUri)?.use { outputStream ->
                                        val buffer = ByteArray(4096)
                                        var bytesRead: Int
                                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                            outputStream.write(buffer, 0, bytesRead)
                                        }
                                        success = true
                                    }
                                }
                            } else {
                                // Para versiones anteriores, usar External Storage
                                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                
                                // Asegurar que el directorio existe
                                if (!downloadsDir.exists()) {
                                    downloadsDir.mkdirs()
                                }
                                
                                val destinationFile = File(downloadsDir, evidence.fileName)
                                
                                // Si el archivo ya existe, agregar número
                                var counter = 1
                                var finalDestinationFile = destinationFile
                                while (finalDestinationFile.exists()) {
                                    val nameWithoutExt = evidence.fileName.substringBeforeLast(".")
                                    val extension = evidence.fileName.substringAfterLast(".", "")
                                    finalDestinationFile = File(downloadsDir, "${nameWithoutExt}_${counter}.${extension}")
                                    counter++
                                }
                                
                                FileOutputStream(finalDestinationFile).use { outputStream ->
                                    val buffer = ByteArray(4096)
                                    var bytesRead: Int
                                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                        outputStream.write(buffer, 0, bytesRead)
                                    }
                                }
                                
                                // Verificar que la descarga fue exitosa
                                success = finalDestinationFile.exists() && finalDestinationFile.length() > 0L
                            }
                            
                            inputStream.close()
                            connection.disconnect()
                            
                            if (success) {
                                Log.i("DOWNLOAD_DEBUG", "✅ Descarga exitosa")
                                true
                            } else {
                                Log.e("DOWNLOAD_DEBUG", "❌ Archivo descargado pero no existe o está vacío")
                                false
                            }
                        } else {
                            Log.e("DOWNLOAD_DEBUG", "❌ Error HTTP: ${connection.responseCode}")
                            false
                        }
                    }
                    
                    // Actualizar el estado en el hilo principal
                    if (result) {
                        // Notificar al sistema que se agregó un archivo (solo para versiones anteriores a Android 10)
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), evidence.fileName)
                            if (file.exists()) {
                                intent.data = Uri.fromFile(file)
                                context.sendBroadcast(intent)
                            }
                        }
                        
                        downloadState = "completed"
                        Log.i("DOWNLOAD_DEBUG", "🎉 Estado cambiado a completed")
                    } else {
                        downloadState = "idle"
                    }
                } catch (e: Exception) {
                    Log.e("DOWNLOAD_DEBUG", "💥 Excepción durante descarga: ${e.message}", e)
                    downloadState = "idle"
                }
            } else {
                Log.i("DOWNLOAD_DEBUG", "⚠️ URL vacía, saltando descarga desde API")
                downloadState = "idle"
            }
        } ?: run {
            Log.i("DOWNLOAD_DEBUG", "🔄 No hay fileUrl, intentando fallback local")
            // Fallback: intentar desde archivo local si existe
            evidence.localPath?.let { localPath ->
                Log.i("DOWNLOAD_DEBUG", "📂 Intentando descarga desde archivo local: $localPath")
                val sourceFile = File(localPath)
                if (sourceFile.exists() && sourceFile.length() > 0L) {
                    try {
                        // Copiar archivo directamente a Downloads
                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        
                        // Asegurar que el directorio existe
                        if (!downloadsDir.exists()) {
                            downloadsDir.mkdirs()
                        }
                        
                        val destinationFile = File(downloadsDir, evidence.fileName)
                        
                        // Si el archivo ya existe, agregar número
                        var counter = 1
                        var finalDestinationFile = destinationFile
                        while (finalDestinationFile.exists()) {
                            val nameWithoutExt = evidence.fileName.substringBeforeLast(".")
                            val extension = evidence.fileName.substringAfterLast(".", "")
                            finalDestinationFile = File(downloadsDir, "${nameWithoutExt}_${counter}.${extension}")
                            counter++
                        }
                        
                        // Copiar el archivo
                        sourceFile.copyTo(finalDestinationFile, overwrite = true)
                        
                        // Verificar que la copia fue exitosa
                        if (finalDestinationFile.exists() && finalDestinationFile.length() > 0L) {
                            // Notificar al sistema que se agregó un archivo
                            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            intent.data = Uri.fromFile(finalDestinationFile)
                            context.sendBroadcast(intent)
                            
                            downloadState = "completed"
                        } else {
                            downloadState = "idle"
                        }
                    } catch (e: Exception) {
                        downloadState = "idle"
                        // Si falla la copia directa, usar ACTION_CREATE_DOCUMENT
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            sourceFile
                        )
                        
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            setType(getMimeType(evidence.fileType))
                            putExtra(Intent.EXTRA_TITLE, evidence.fileName)
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        
                        context.startActivity(intent)
                    }
                } else {
                    Log.e("DOWNLOAD_DEBUG", "❌ Archivo local no existe o está vacío: $localPath")
                    downloadState = "idle"
                }
            } ?: run {
                Log.i("DOWNLOAD_DEBUG", "⚠️ No hay localPath disponible")
                downloadState = "idle"
            }
        }
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header con botón cerrar y título
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }
                    
                    Text(
                        text = "Galeria",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    // Espaciador para centrar el título
                    Spacer(modifier = Modifier.size(40.dp))
                }
                
                // Área principal de previsualización
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Log.i("MODAL_DEBUG", "🔍 Modal - Tipo de archivo: '${evidence.fileType}' para ${evidence.fileName}")
                    
                    // Detectar imágenes por tipo o extensión del archivo
                    val isImage = evidence.fileType.equals("image", ignoreCase = true) ||
                                 evidence.fileType.uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP") ||
                                 evidence.fileName.substringAfterLast(".").uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP")
                    
                    Log.i("MODAL_DEBUG", "🔍 Modal - Es imagen? $isImage")
                    
                    if (isImage) {
                        // Mostrar icono para imágenes (sin preview para evitar sobrecargar recursos)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Imagen",
                                modifier = Modifier.size(120.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Imagen",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = evidence.fileName,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Usa el botón de descarga para ver el archivo",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Mostrar icono para archivos no-imagen
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = evidence.fileType,
                                modifier = Modifier.size(120.dp),
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (evidence.fileType.equals("multipart", ignoreCase = true)) "Documento" else evidence.fileType,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = evidence.fileName,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // Información del archivo
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = evidence.fileName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = DateFormatter.formatDateShort(evidence.uploadDate),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = if (evidence.uploadTime.isNotEmpty()) {
                                DateFormatter.formatDate(evidence.uploadTime)
                            } else {
                                DateFormatter.formatDate(evidence.uploadDate)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botón de descarga
                    Button(
                        onClick = { 
                            Log.i("DOWNLOAD_DEBUG", "🖱️ Botón de descarga presionado")
                            Log.i("DOWNLOAD_DEBUG", "📊 Estado actual del botón: $downloadState")
                            if (downloadState == "idle") {
                                Log.i("DOWNLOAD_DEBUG", "✅ Estado es idle, llamando downloadFile()")
                                // Lanzar corrutina para la descarga
                                coroutineScope.launch {
                                    downloadFile()
                                }
                            } else {
                                Log.i("DOWNLOAD_DEBUG", "⏸️ Estado no es idle, no se ejecuta descarga")
                            }
                        },
                        enabled = downloadState != "downloading",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (downloadState) {
                                "completed" -> Color(0xFF4CAF50) // Verde para completado
                                else -> Color.Black
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        when (downloadState) {
                            "downloading" -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Descargando...",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            "completed" -> {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Descargado",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Descargado",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            else -> {
                                val hasUrl = !evidence.fileUrl.isNullOrEmpty() || !evidence.localPath.isNullOrEmpty()
                                if (hasUrl) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Descargar",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Descargar",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = "Sin URL",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Sin URL disponible",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
