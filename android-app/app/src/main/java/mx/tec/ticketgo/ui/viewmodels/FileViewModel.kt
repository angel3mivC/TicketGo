package mx.tec.ticketgo.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import mx.tec.ticketgo.data.repository.FilesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class FileItem(
    val id: String,
    val name: String,
    val size: Long,
    val uri: Uri,
    val type: String,
    val localPath: String? = null
)

class FileViewModel : ViewModel() {
    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()
    
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()
    
    private val _uploadMessage = MutableStateFlow<String?>(null)
    val uploadMessage: StateFlow<String?> = _uploadMessage.asStateFlow()
    
    private val maxTotalSize = 100 * 1024 * 1024L // 100 MB
    
    // Repositorio para llamadas a la API
    private val filesRepository = FilesRepository()
    
    // Referencia al EvidenceViewModel para sincronizar
    private var evidenceViewModel: EvidenceViewModel? = null
    
    fun setEvidenceViewModel(evidenceViewModel: EvidenceViewModel) {
        this.evidenceViewModel = evidenceViewModel
    }
    
    // Función para extraer solo el mensaje útil de los errores
    private fun cleanErrorMessage(errorMessage: String?): String {
        if (errorMessage == null) return "Error desconocido"
        
        return try {
            // Buscar JSON en el mensaje
            val jsonRegex = Regex("""\{[^}]*"message"[^}]*\}""")
            val jsonMatch = jsonRegex.find(errorMessage)
            
            if (jsonMatch != null) {
                // Extraer y parsear el JSON encontrado
                try {
                    JSONObject(jsonMatch.value).optString("message", errorMessage)
                } catch (_: Exception) {
                    errorMessage
                }
            } else {
                // Si no hay JSON, limpiar prefijos comunes
                when {
                    errorMessage.startsWith("error->") -> errorMessage.substring(7)
                    errorMessage.startsWith("Error al subir") -> errorMessage
                    errorMessage.contains("HTTP") -> {
                        // Para mensajes HTTP, intentar extraer la parte útil después del guion
                        val parts = errorMessage.split(" - ")
                        if (parts.size > 1) parts.last() else errorMessage
                    }
                    else -> errorMessage
                }
            }
        } catch (_: Exception) {
            errorMessage
        }
    }
    
    fun addFile(context: Context, uri: Uri, ticketId: Int) {
        viewModelScope.launch {
            try {
                val fileItem = createFileItem(context, uri)
                if (fileItem != null) {
                    val currentFiles = _files.value.toMutableList()
                    
                    // Verificar tamaño total
                    val currentTotalSize = currentFiles.sumOf { it.size }
                    if (currentTotalSize + fileItem.size > maxTotalSize) {
                        _uploadMessage.value = "El tamaño total excede el límite de 100 MB"
                        return@launch
                    }
                    
                    // Verificar si el archivo ya existe
                    if (currentFiles.any { it.name == fileItem.name }) {
                        _uploadMessage.value = "El archivo ya existe"
                        return@launch
                    }
                    
                    // Copiar archivo al almacenamiento local con carpeta específica del ticket
                    val localPath = copyFileToLocalStorage(context, uri, fileItem.name, ticketId)
                    val fileItemWithPath = fileItem.copy(localPath = localPath)
                    
                    currentFiles.add(fileItemWithPath)
                    _files.value = currentFiles
                    _uploadMessage.value = null
                }
            } catch (e: Exception) {
                _uploadMessage.value = "Error al procesar el archivo: ${cleanErrorMessage(e.message)}"
            }
        }
    }
    
    fun removeFile(fileId: String) {
        val currentFiles = _files.value.toMutableList()
        currentFiles.removeAll { it.id == fileId }
        _files.value = currentFiles
    }
    
    fun uploadFiles(ticketId: Int) {
        viewModelScope.launch {
            _isUploading.value = true
            Log.i("FILE_UPLOAD", "🚀 Iniciando subida de archivos para ticket $ticketId")
            try {
                val currentFiles = _files.value
                Log.i("FILE_UPLOAD", "📁 Archivos a subir: ${currentFiles.size}")
                var successCount = 0
                var errorCount = 0
                
                // Subir cada archivo a la API usando multipart
                for (file in currentFiles) {
                    Log.i("FILE_UPLOAD", "📤 Procesando archivo: ${file.name}")
                    try {
                        if (file.localPath != null) {
                            val fileObj = File(file.localPath)
                            Log.i("FILE_UPLOAD", "📂 Archivo existe: ${fileObj.exists()}, tamaño: ${fileObj.length()}")
                            if (fileObj.exists()) {
                                // Crear MultipartBody.Part
                                val mediaType = getMediaType(file.type)
                                Log.i("FILE_UPLOAD", "🎭 Media type: $mediaType")
                                
                                val requestFile = fileObj.asRequestBody(
                                    mediaType.toMediaTypeOrNull()
                                )
                                // Probar diferentes nombres de campo que el servidor podría esperar
                                val fieldNames = listOf("archivo", "file", "upload", "documento", "attachment")
                                var uploadSuccess = false
                                
                                for (fieldName in fieldNames) {
                                    if (uploadSuccess) break
                                    
                                    Log.i("FILE_UPLOAD", "🔄 Probando campo: $fieldName")
                                    val filePart = MultipartBody.Part.createFormData(
                                        fieldName, 
                                        file.name, 
                                        requestFile
                                    )
                                    
                                    Log.i("FILE_UPLOAD", "📡 Enviando archivo a API con campo '$fieldName'...")
                                    Log.i("FILE_UPLOAD", "📋 Detalles del archivo: nombre=${file.name}, tipo=${file.type}, tamaño=${fileObj.length()}")
                                    
                                    // Crear RequestBody para metadatos adicionales
                                    val fileNameBody = file.name.toRequestBody("text/plain".toMediaTypeOrNull())
                                    val fileTypeBody = file.type.toRequestBody("text/plain".toMediaTypeOrNull())
                                    
                                    Log.i("FILE_UPLOAD", "📋 Metadatos adicionales: nombre_archivo='${file.name}', tipo_archivo='${file.type}'")
                                    
                                    val result = filesRepository.uploadFile(ticketId, filePart, fileNameBody, fileTypeBody)
                                    result.fold(
                                        onSuccess = { response ->
                                            Log.i("FILE_UPLOAD", "✅ Archivo subido exitosamente con campo '$fieldName': ${response.message}")
                                            successCount++
                                            uploadSuccess = true
                                            // Agregar evidencia al EvidenceViewModel
                                            evidenceViewModel?.addEvidence(
                                                ticketId = ticketId,
                                                fileName = file.name,
                                                fileType = file.type,
                                                fileSize = file.size,
                                                localPath = null // Ya no necesitamos path local
                                            )
                                        },
                                        onFailure = { error ->
                                            Log.e("FILE_UPLOAD", "❌ Error con campo '$fieldName': ${error.message}")
                                            if (fieldName == fieldNames.last()) {
                                                errorCount++
                                                _uploadMessage.value = "Error al subir ${file.name}: ${cleanErrorMessage(error.message)}"
                                            }
                                        }
                                    )
                                }
                            } else {
                                Log.e("FILE_UPLOAD", "❌ Archivo no encontrado: ${file.localPath}")
                                errorCount++
                                _uploadMessage.value = "Archivo no encontrado: ${file.name}"
                            }
                        } else {
                            Log.e("FILE_UPLOAD", "❌ Ruta local no disponible para: ${file.name}")
                            errorCount++
                            _uploadMessage.value = "Ruta local no disponible para: ${file.name}"
                        }
                    } catch (e: Exception) {
                        Log.e("FILE_UPLOAD", "💥 Excepción al procesar ${file.name}: ${e.message}", e)
                        errorCount++
                        _uploadMessage.value = "Error al procesar ${file.name}: ${cleanErrorMessage(e.message)}"
                    }
                }
                
                Log.i("FILE_UPLOAD", "📊 Resultado final: $successCount exitosos, $errorCount fallidos")
                
                // Mostrar resultado final
                when {
                    successCount > 0 && errorCount == 0 -> {
                        _uploadMessage.value = "Todos los archivos se subieron exitosamente"
                        _files.value = emptyList()
                    }
                    successCount > 0 && errorCount > 0 -> {
                        _uploadMessage.value = "$successCount archivos subidos, $errorCount fallaron"
                    }
                    else -> {
                        _uploadMessage.value = "Error al subir archivos"
                    }
                }
                
            } catch (e: Exception) {
                Log.e("FILE_UPLOAD", "💥 Error general al subir archivos: ${e.message}", e)
                _uploadMessage.value = "Error general al subir archivos: ${cleanErrorMessage(e.message)}"
            } finally {
                _isUploading.value = false
            }
        }
    }
    
    fun clearMessage() {
        _uploadMessage.value = null
    }
    
    private fun createFileItem(context: Context, uri: Uri): FileItem? {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
                
                it.moveToFirst()
                val name = if (nameIndex >= 0) it.getString(nameIndex) else "archivo"
                val size = if (sizeIndex >= 0) it.getLong(sizeIndex) else 0L
                
                val extension = name.substringAfterLast('.', "").uppercase()
                
                FileItem(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    size = size,
                    uri = uri,
                    type = extension
                )
            }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getTotalSize(): Long {
        return _files.value.sumOf { it.size }
    }
    
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }
    
    private fun copyFileToLocalStorage(context: Context, uri: Uri, fileName: String, ticketId: Int): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputDir = File(context.filesDir, "evidence_files")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            
            // Crear carpeta específica para el ticket
            val ticketDir = File(outputDir, "ticket_$ticketId")
            if (!ticketDir.exists()) {
                ticketDir.mkdirs()
            }
            
            val outputFile = File(ticketDir, fileName)
            val outputStream = FileOutputStream(outputFile)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            outputFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getMediaType(fileType: String): String {
        return when (fileType.uppercase()) {
            "JPG", "JPEG" -> "image/jpeg"
            "PNG" -> "image/png"
            "GIF" -> "image/gif"
            "WEBP" -> "image/webp"
            "PDF" -> "application/pdf"
            "DOC" -> "application/msword"
            "DOCX" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "XLS" -> "application/vnd.ms-excel"
            "XLSX" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "TXT" -> "text/plain"
            "ZIP" -> "application/zip"
            "RAR" -> "application/x-rar-compressed"
            else -> "application/octet-stream"
        }
    }
}