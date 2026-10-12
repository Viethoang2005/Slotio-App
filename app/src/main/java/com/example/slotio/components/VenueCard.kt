package com.example.slotio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.slotio.theme.xanhNgocLucBao


@Composable
fun VenueCard(
    name: String,
    description: String,
    address: String,
    phone: String,
    imageUrl: String,
) {
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = if(isFavorite) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if(isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.height(5.dp))
            Column(
                modifier = Modifier
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = styledText("Address: ", address),
                    fontSize = 15.sp
                )
                Text(
                    text = styledText("Phone: ", phone),
                    fontSize = 15.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = xanhNgocLucBao
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        onClick = {}
                    ) {
                        Text(
                            text = "Dat lich",
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun VenueCardPreview() {
    VenueCard(
        name = "Sân Cầu Lông ABC",
        description = "Sân đạt chuẩn quốc tế, thoáng mát, có máy lạnh.",
        address = "Số 12, Đường Lê Duẩn, Quận 1, TP.HCM",
        phone = "0912 345 678",
        imageUrl = "https://images.unsplash.com/photo-1626231752423-15c63d8d8a4d?q=80&w=1000&auto=format&fit=crop" // Link ảnh mẫu
    )
}

fun styledText(label: String, value: String): AnnotatedString {
    val styledText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(label)
        }
        append(value)
    }
    return styledText
}