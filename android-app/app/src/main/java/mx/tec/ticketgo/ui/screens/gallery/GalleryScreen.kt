package mx.tec.ticketgo.ui.screens.gallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.ticketgo.ui.components.EvidenceDetailModal
import mx.tec.ticketgo.ui.components.GalleryItem
import mx.tec.ticketgo.ui.viewmodels.EvidenceViewModel

@Composable
fun GalleryScreen(
    ticketId: Int,
    evidenceViewModel: EvidenceViewModel = viewModel()
) {
    val context = LocalContext.current
    val evidences by evidenceViewModel.evidences.collectAsStateWithLifecycle()
    val isLoading by evidenceViewModel.isLoading.collectAsStateWithLifecycle()
    val error by evidenceViewModel.error.collectAsStateWithLifecycle()
    
    // Estado para el modal
    var selectedEvidence by remember { mutableStateOf<mx.tec.ticketgo.data.models.Evidence?>(null) }
    
    // Cargar evidencias cuando se monta el componente
    LaunchedEffect(ticketId) {
        // Cargar evidencias desde la API
        evidenceViewModel.loadEvidencesFromApi(ticketId)
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                // Mostrar indicador de carga
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            error != null -> {
                // Mostrar mensaje de error
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar evidencias",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            evidences.isEmpty() -> {
                // Mostrar mensaje cuando no hay evidencias
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay archivos para mostrar",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
            
            else -> {
                // Mostrar grid de evidencias
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(evidences) { evidence ->
                        GalleryItem(
                            evidence = evidence,
                            onClick = {
                                selectedEvidence = evidence
                            }
                        )
                    }
                }
            }
        }
        
        // Modal de detalle
        selectedEvidence?.let { evidence ->
            EvidenceDetailModal(
                evidence = evidence,
                onDismiss = {
                    selectedEvidence = null
                },
                onDownload = {
                    // La descarga se maneja internamente en el modal
                }
            )
        }
    }
}