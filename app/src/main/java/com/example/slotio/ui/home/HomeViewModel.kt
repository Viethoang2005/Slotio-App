package com.example.slotio.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slotio.data.model.Sport
import com.example.slotio.data.model.UserSession
import com.example.slotio.data.model.Venue
import com.example.slotio.data.repository.SportRepository
import com.example.slotio.data.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

data class HomeUiState(
    val isLoading: Boolean = false,
    val venues: List<Venue> = emptyList(),
    val favoriteVenus: List<Venue> = emptyList(),
    val sports: List<Sport> = emptyList(),
    var selectedSportId: Int? = null,
    var isFavoriteFilterActive: Boolean = false,
    val error: String? = null
){
    val displayedVenues : List<Venue>
        get() = if (isFavoriteFilterActive) favoriteVenus else venues
}

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val venueRepository: VenueRepository,
    private val sportRepository: SportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadSports()
        loadVenues()
        if (UserSession.token.isNotBlank()) {
            loadFavoriteVenues(UserSession.token, silent = true)
        }
        setupSearch()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearch(){
        viewModelScope.launch {
            searchQueryFlow
                .debounce(400.milliseconds)
                .distinctUntilChanged()
                .collect { query ->
                    if(query.isNotBlank()) {
                        executeSearch(query)
                    }
                }
        }
    }

    private fun executeSearch(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    venues = emptyList()
                )
            }

            try {
                val response = venueRepository.searchVenues(query)
                val favIds = _uiState.value.favoriteVenus.map { it.id }.toSet()
                val updatedVenues = response.map { venue ->
                    venue.copy(isFavorite = favIds.contains(venue.id))
                }
                _uiState.update {
                    it.copy (
                        isLoading = false,
                        venues = updatedVenues,
                        error = null
                    )
                }
            } catch(e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun loadSports() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val response = sportRepository.getSports()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sports = response,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun loadVenues(sportId: Int? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }
            try {
                val response = venueRepository.getVenues(sportId)
                val favIds = _uiState.value.favoriteVenus.map { it.id }.toSet()
                val updatedVenues = response.map { venue ->
                    venue.copy(isFavorite = favIds.contains(venue.id))
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        venues = updatedVenues,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Khong the tai danh sach san"
                    )
                }
            }
        }
    }

    fun searchVenues(query: String) {
        searchQueryFlow.value = query
    }

    fun selectSport(sportId: Int?) {
        val newSelectedId = if(_uiState.value.selectedSportId == sportId) null else sportId
        _uiState.update {
            it.copy(
                selectedSportId = newSelectedId
            )
        }
        loadVenues(newSelectedId)
    }

    fun toggleFavoriteFilter() {
        val willBeActive = !_uiState.value.isFavoriteFilterActive

        if (willBeActive && UserSession.token.isBlank()) {
            _uiState.update { it.copy(error = "Vui lòng đăng nhập để xem danh sách yêu thích") }
            return
        }

        _uiState.update { it.copy(isFavoriteFilterActive = willBeActive, error = null) }

        if (willBeActive) {
            loadFavoriteVenues(UserSession.token, silent = false)
        }
    }

    fun loadFavoriteVenues(token: String, silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            try {
                val cleanToken = token.removePrefix("Bearer ").trim()
                if (cleanToken.isBlank()) {
                    if (!silent) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Vui lòng đăng nhập để xem sân yêu thích"
                            )
                        }
                    }
                    return@launch
                }

                val authHeader = "Bearer $cleanToken"
                val response = venueRepository.getFavoriteVenues(authHeader)
                if (response.isSuccessful && response.body() != null) {
                    val rawList = response.body()?.data ?: emptyList()
                    val listFav = rawList.map { it.copy(isFavorite = true) }
                    val favIds = listFav.map { it.id }.toSet()

                    _uiState.update { currentState ->
                        val updatedVenues = currentState.venues.map { venue ->
                            venue.copy(isFavorite = favIds.contains(venue.id))
                        }
                        currentState.copy(
                            isLoading = false,
                            favoriteVenus = listFav,
                            venues = updatedVenues,
                            error = null
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Lỗi ${response.code()}: $errorBody"
                        )
                    }
                }
            } catch (e: Exception) {
                val errorMsg = if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    "Lỗi HTTP ${e.code()}: $errorBody"
                } else {
                    e.message ?: "Unknown error"
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                }
            }
        }
    }

    fun toggleFavorite(venueId: Int, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            val token = UserSession.token.removePrefix("Bearer ").trim()
            if (token.isBlank()) {
                _uiState.update { it.copy(error = "Vui lòng đăng nhập để thêm hoặc xóa sân yêu thích") }
                return@launch
            }

            val authHeader = "Bearer $token"
            val isCurrentlyFav = _uiState.value.favoriteVenus.any { it.id == venueId } || currentIsFavorite
            val newIsFavorite = !isCurrentlyFav

            // Optimistic update
            _uiState.update { currentState ->
                val updatedVenues = currentState.venues.map { venue ->
                    if (venue.id == venueId) venue.copy(isFavorite = newIsFavorite) else venue
                }
                val updatedFavorites = if (newIsFavorite) {
                    val venueToAdd = currentState.venues.find { it.id == venueId }
                        ?: currentState.favoriteVenus.find { it.id == venueId }
                    if (venueToAdd != null) {
                        val item = venueToAdd.copy(isFavorite = true)
                        if (currentState.favoriteVenus.none { it.id == venueId }) {
                            currentState.favoriteVenus + item
                        } else {
                            currentState.favoriteVenus.map { if (it.id == venueId) item else it }
                        }
                    } else {
                        currentState.favoriteVenus
                    }
                } else {
                    currentState.favoriteVenus.filter { it.id != venueId }
                }

                currentState.copy(
                    venues = updatedVenues,
                    favoriteVenus = updatedFavorites,
                    error = null
                )
            }

            try {
                if (isCurrentlyFav) {
                    venueRepository.removeFavorite(authHeader, venueId)
                } else {
                    venueRepository.addFavorite(authHeader, venueId)
                }
            } catch (e: Exception) {
                // Rollback nếu API lỗi
                _uiState.update { currentState ->
                    val rollbackVenues = currentState.venues.map { venue ->
                        if (venue.id == venueId) venue.copy(isFavorite = isCurrentlyFav) else venue
                    }
                    val rollbackFavorites = if (isCurrentlyFav) {
                        val venueToAdd = currentState.venues.find { it.id == venueId }
                        if (venueToAdd != null && currentState.favoriteVenus.none { it.id == venueId }) {
                            currentState.favoriteVenus + venueToAdd.copy(isFavorite = true)
                        } else {
                            currentState.favoriteVenus
                        }
                    } else {
                        currentState.favoriteVenus.filter { it.id != venueId }
                    }

                    val errorMsg = if (e is retrofit2.HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        "Lỗi HTTP ${e.code()}: $errorBody"
                    } else {
                        e.message ?: "Lỗi cập nhật yêu thích"
                    }

                    currentState.copy(
                        venues = rollbackVenues,
                        favoriteVenus = rollbackFavorites,
                        error = errorMsg
                    )
                }
            }
        }
    }
}
