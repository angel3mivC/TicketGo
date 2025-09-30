package mx.tec.ticketgo.ui.screens.gallery

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.components.GalleryItem

@Composable
fun GalleryScreen(){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(5) { index ->
            GalleryItem(
                title = "Titulo $index",
                date = "Hoy",
                time = "Ahorita"
            )

        }
    }
}