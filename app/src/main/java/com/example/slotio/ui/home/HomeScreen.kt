package com.example.slotio.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.slotio.R
import com.example.slotio.components.SportItem
import com.example.slotio.components.VenueCard
import com.example.slotio.data.model.Sport
import com.example.slotio.data.model.UserSession
import com.example.slotio.theme.xanhNgocLucBao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearchScreen: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(uiState = uiState,
        onNavigateToSearchScreen = onNavigateToSearchScreen,
        onSportClick = { sportId ->
            viewModel.selectSport(sportId)
        })
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateToSearchScreen: () -> Unit,
    onSportClick: (Int) -> Unit
) {
    var favoriteState by remember { mutableStateOf(false) }
    val currentDateStr = remember { getCurrentDate() }
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(xanhNgocLucBao)
                    .statusBarsPadding()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(6.dp)
                                .clip(CircleShape),
                            painter = painterResource(R.drawable.icon),
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = currentDateStr, color = Color.White)
                            Text(text = UserSession.userName.ifEmpty {"Khach"}, color = Color.White)
                        }

                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color.White,
                                containerColor = Color.Transparent
                            ),
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth()
                                .height(56.dp)
                                .weight(1f)
                                .clickable {
                                    onNavigateToSearchScreen()
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Tim kiem...",
                                    color = Color.Gray
                                )
                            }

                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            IconButton(
                                onClick = { favoriteState = !favoriteState }
                            ) {
                                Icon(
                                    imageVector = if (!favoriteState) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                                    contentDescription = "Favorite",
                                    tint = if (favoriteState) Color.Red else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.sports, key = {it.id}) { sport ->
                            SportItem(
                                sport = sport,
                                selected = uiState.selectedSportId == sport.id,
                                onClick = { onSportClick(sport.id) }
                            )

                        }
                    }
                }

                itemsIndexed(uiState.venues) { _, venue ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        VenueCard(
                            venue.name,
                            venue.description,
                            venue.address,
                            venue.phone,
                            venue.imageUrl
                        )
                    }
                }

            }
            if(uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.error?.let { errorMessage ->
                Text(
                    text = "Loi: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val sampleState = HomeUiState(
        sports = listOf(
            Sport(1, "badminton", imageUrl = "https://res.cloudinary.com/cmatwqd9/image/upload/v1790688418/badminton_image.jpg"),
            Sport(2, "tennis", imageUrl = "https://res.cloudinary.com/cmatwqd9/image/upload/v1790688418/tennis_image.jpg")
        ),
        isLoading = false
    )
    HomeScreenContent(uiState = sampleState, onNavigateToSearchScreen = {}, onSportClick = {})
}

fun getCurrentDate() : String {
    val vietnameseLocate = Locale.forLanguageTag("vi-VN")

    val sdf = SimpleDateFormat("EEEE, dd/MM/yyyy", vietnameseLocate)
    val formatted = sdf.format(Date())

    return formatted.replaceFirstChar {
        if(it.isLowerCase())
            it.titlecase(vietnameseLocate)
        else
            it.toString()
    }
}

