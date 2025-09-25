package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import mx.tec.ticketgo.ui.theme.EmptyElement

@Composable
fun GalleryItem(title: String, date: String, time: String){
    Column(
        modifier = Modifier.padding(8.dp).clip(RoundedCornerShape(12.dp))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f/1.5f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.EmptyElement)
        )

        Text(text = title)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(date)
            Text(time)
        }
    }
}