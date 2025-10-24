package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import mx.tec.ticketgo.data.models.Evidence
import mx.tec.ticketgo.ui.theme.EmptyElement
import android.util.Log
import androidx.compose.ui.text.style.TextAlign
import mx.tec.ticketgo.ui.theme.CardBackground

@Composable
fun GalleryItem(
    evidence: Evidence,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Log.i("GALLERY_DEBUG", "🖼️ Renderizando GalleryItem: ${evidence.fileName}")
    Log.i("GALLERY_DEBUG", "📁 FileUrl: ${evidence.fileUrl}")
    Log.i("GALLERY_DEBUG", "🖼️ ThumbnailUrl: ${evidence.thumbnailUrl}")
    Log.i("GALLERY_DEBUG", "📂 LocalPath: ${evidence.localPath}")
    Log.i("GALLERY_DEBUG", "🏷️ FileType: ${evidence.fileType}")
    
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.CardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Área de previsualización
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1.2f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.EmptyElement),
                contentAlignment = Alignment.Center
            ) {
                Log.i("GALLERY_DEBUG", "🔍 Tipo de archivo: '${evidence.fileType}' para ${evidence.fileName}")
                
                // Detectar imágenes por tipo o extensión del archivo
                val isImage = evidence.fileType.equals("image", ignoreCase = true) ||
                             evidence.fileType.uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP") ||
                             evidence.fileName.substringAfterLast(".").uppercase() in listOf("JPG", "JPEG", "PNG", "GIF", "WEBP")
                
                Log.i("GALLERY_DEBUG", "🔍 Es imagen? $isImage")
                
                if (isImage) {
                    // Mostrar icono para imágenes (sin preview para evitar sobrecargar recursos)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Imagen",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Imagen",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                } else {
                    // Mostrar icono para archivos no-imagen (PDF, DOC, etc.)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = evidence.fileType,
                            modifier = Modifier.size(48.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (evidence.fileType.equals("multipart", ignoreCase = true)) "Documento" else evidence.fileType,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Información del archivo
            Text(
                text = evidence.fileName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Fecha y hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = evidence.uploadDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = evidence.uploadTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}