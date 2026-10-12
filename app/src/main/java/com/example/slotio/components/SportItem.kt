package com.example.slotio.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.slotio.data.model.Sport
import com.example.slotio.theme.xanhNgocLucBao

@Composable
fun SportItem(
    sport: Sport,
    selected: Boolean,
    onClick: () -> Unit
) {
   val borderStroke = if(selected) BorderStroke(2.dp, xanhNgocLucBao) else null
    Card(
        elevation = CardDefaults.elevatedCardElevation(if(selected) 4.dp else 1.dp),
        shape = RoundedCornerShape(16.dp),
        border = borderStroke,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .widthIn(min = 80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = sport.imageUrl,
                contentDescription = sport.name,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = sport.name,
                fontSize = 13.sp,
                fontWeight = if(selected) FontWeight.Bold else FontWeight.Medium,
                color = Color.Black,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun SportItemPreview() {
    SportItem(
        sport = Sport(1, "Badminton", ""),
        selected = false,
        onClick = {}
    )
}