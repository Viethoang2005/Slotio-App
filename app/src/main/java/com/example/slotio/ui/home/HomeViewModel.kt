package com.example.slotio.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slotio.data.model.Sport
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
    val sports: List<Sport> = emptyList(),
    var selectedSportId: Int? = null,
    val error: String? = null
)

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
                _uiState.update {
                    it.copy (
                        isLoading = false,
                        venues = response,
                        error = null
                    )
                }
            }catch(e: Exception) {
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
            }catch (e: Exception) {
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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        venues = response,
                        error = null
                    )
                }
            }catch (e: Exception) {
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

}

