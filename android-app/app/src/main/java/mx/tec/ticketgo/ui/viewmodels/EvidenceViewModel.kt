package mx.tec.ticketgo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.tec.ticketgo.data.models.Evidence
import mx.tec.ticketgo.data.models.File
import mx.tec.ticketgo.data.repository.FilesRepository
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

class EvidenceViewModel : ViewModel() {
    private val _evidences = MutableStateFlow<List<Evidence>>(emptyList())
    val evidences: StateFlow<List<Evidence>> = _evidences.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Almacenar evidencias por ticket
    private val evidencesByTicket = mutableMapOf<Int, MutableList<Evidence>>()
    
    // Repositorio para llamadas a la API
    private val filesRepository = FilesRepository()
    
    fun getEvidences(ticketId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // TODO: Implementar llamada a la API para obtener evidencias
                // Por ahora solo mostramos evidencias locales, sin datos mock
                val ticketEvidences = evidencesByTicket[ticketId] ?: mutableListOf()
                _evidences.value = ticketEvidences.toList()
            } catch (e: Exception) {
                _error.value = "Error al cargar evidencias: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addEvidence(ticketId: Int, fileName: String, fileType: String, fileSize: Long, localPath: String? = null) {
        viewModelScope.launch {
            val currentEvidences = evidencesByTicket[ticketId]?.toMutableList() ?: mutableListOf()
            
            val newEvidence = Evidence(
                id = System.currentTimeMillis().toInt(), // ID temporal
                fileName = fileName,
                fileType = fileType.uppercase(),
                fileSize = fileSize,
                uploadDate = getCurrentDate(),
                uploadTime = getCurrentTime(),
                fileUrl = localPath, // Usar ruta local si está disponible, sino será null
                thumbnailUrl = if (fileType.uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP")) {
                    localPath // Para imágenes, usar la misma ruta local
                } else null,
                localPath = localPath
            )
            
            currentEvidences.add(newEvidence)
            evidencesByTicket[ticketId] = currentEvidences
            _evidences.value = currentEvidences.toList()
        }
    }
    
    private fun createMockEvidences(ticketId: Int): List<Evidence> {
        return listOf(
            Evidence(
                id = 1,
                fileName = "imagen1.jpg",
                fileType = "JPG",
                fileSize = 1024000L,
                uploadDate = "Hoy",
                uploadTime = "14:30",
                fileUrl = "https://example.com/imagen1.jpg",
                thumbnailUrl = "https://example.com/thumb1.jpg"
            ),
            Evidence(
                id = 2,
                fileName = "documento.pdf",
                fileType = "PDF",
                fileSize = 2048000L,
                uploadDate = "Ayer",
                uploadTime = "10:15",
                fileUrl = "https://example.com/documento.pdf"
            ),
            Evidence(
                id = 3,
                fileName = "foto2.png",
                fileType = "PNG",
                fileSize = 1536000L,
                uploadDate = "Hoy",
                uploadTime = "16:45",
                fileUrl = "https://example.com/foto2.png",
                thumbnailUrl = "https://example.com/thumb2.png"
            ),
            Evidence(
                id = 4,
                fileName = "reporte.pdf",
                fileType = "PDF",
                fileSize = 3072000L,
                uploadDate = "Hace 2 días",
                uploadTime = "09:20",
                fileUrl = "https://example.com/reporte.pdf"
            )
        )
    }
    
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    
    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
    
    fun clearError() {
        _error.value = null
    }
    
    // Método para cargar evidencias desde la API
    fun loadEvidencesFromApi(ticketId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Llamar a la API para obtener archivos del ticket
                val result = filesRepository.getTickets(ticketId)
                
                result.fold(
                    onSuccess = { files ->
                        Log.i("EVIDENCE_LOAD", "📁 Archivos recibidos de la API: ${files.size}")
                        Log.i("EVIDENCE_LOAD", "🔍 Respuesta completa de la API: $files")
                        // Convertir archivos de la API a evidencias
                        val apiEvidences = files.map { file ->
                            Log.i("EVIDENCE_LOAD", "📄 Archivo: ${file.nombre_archivo}, Tipo: ${file.tipo_archivo}")
                            Log.i("EVIDENCE_LOAD", "🔍 Archivo completo: $file")
                            
                            // Construir URL de descarga agregando /download al endpoint
                            val downloadUrl = "http://ticket-env.eba-3gvvmzhz.us-east-1.elasticbeanstalk.com/tickets/$ticketId/files/${file.id_adjunto}/download"
                            Log.i("EVIDENCE_LOAD", "🔗 URL de descarga construida: $downloadUrl")
                            
                            Evidence(
                                id = file.id_adjunto,
                                fileName = file.nombre_archivo,
                                fileType = file.tipo_archivo,
                                fileSize = 0L, // La API no devuelve tamaño
                                uploadDate = file.fecha,
                                uploadTime = "", // La API no devuelve hora
                                fileUrl = downloadUrl, // URL de descarga construida
                                thumbnailUrl = if (file.tipo_archivo.uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP")) {
                                    downloadUrl // Para imágenes, usar la misma URL
                                } else null,
                                localPath = null // Ya no usamos path local
                            )
                        }
                        
                        Log.i("EVIDENCE_LOAD", "✅ Evidencias creadas: ${apiEvidences.size}")
                        evidencesByTicket[ticketId] = apiEvidences.toMutableList()
                        _evidences.value = apiEvidences
                    },
                    onFailure = { error ->
                        _error.value = "Error al cargar evidencias desde la API: ${error.message}"
                    }
                )
            } catch (e: Exception) {
                _error.value = "Error al cargar evidencias desde la API: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
