package com.example.slotio.ui.home.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.slotio.R
import com.example.slotio.components.VenueCard
import com.example.slotio.data.model.Venue
import com.example.slotio.theme.xanhNgocLucBao
import com.example.slotio.ui.home.HomeUiState
import com.example.slotio.ui.home.HomeViewModel

@Composable
fun SearchScreen(
    onBackClick :() -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    SearchScreenContent(
        uiState = uiState,
        onSearchQueryChanged = { query ->
            viewModel.searchVenues(query)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: HomeUiState,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var inputSearch by remember { mutableStateOf("") }
    val focusRequest = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequest.requestFocus()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Tim kiem",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = xanhNgocLucBao
                )
            )
        }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    elevation = CardDefaults.cardElevation(15.dp)
                ) {
                    TextField(
                        value = inputSearch,
                        onValueChange = { newValue ->
                            inputSearch = newValue
                            onSearchQueryChanged(newValue)
                        },
                        placeholder = { Text("Nhap ten san muon tim...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        leadingIcon = {
                            Image(
                                painter = painterResource(R.drawable.icon),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequest)
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.venues) { venue ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            VenueCard(
                                name = venue.name,
                                description = venue.description,
                                address = venue.address,
                                phone = venue.phone,
                                imageUrl = venue.imageUrl,
                                isFavorite = venue.isFavorite,
                                onFavoriteClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    val sampleState = HomeUiState(
        isLoading = false,
        venues = listOf(
            Venue(
                id = 1,
                name = "Apex Badminton",
                description = "San cau long hien dai",
                address = "175 Tay Son, Kim Lien",
                phone = "0987654321",
                imageUrl = "",
                isFavorite = false
            )
        )
    )
    SearchScreenContent(uiState = sampleState, onSearchQueryChanged = {},
        onBackClick = {})
}