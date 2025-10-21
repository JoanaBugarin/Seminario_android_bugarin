package com.seminario.videojuegosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seminario.videojuegosapp.data.model.GameFilters
import com.seminario.videojuegosapp.data.model.Genre
import com.seminario.videojuegosapp.data.model.PlatformInfo
import com.seminario.videojuegosapp.data.model.SortOption
import com.seminario.videojuegosapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {
    
    private val _platforms = MutableStateFlow<List<PlatformInfo>>(emptyList())
    val platforms: StateFlow<List<PlatformInfo>> = _platforms.asStateFlow()
    
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()
    
    private val _selectedPlatforms = MutableStateFlow<List<String>>(emptyList())
    val selectedPlatforms: StateFlow<List<String>> = _selectedPlatforms.asStateFlow()
    
    private val _selectedGenres = MutableStateFlow<List<String>>(emptyList())
    val selectedGenres: StateFlow<List<String>> = _selectedGenres.asStateFlow()
    
    private val _selectedSortOption = MutableStateFlow<SortOption?>(null)
    val selectedSortOption: StateFlow<SortOption?> = _selectedSortOption.asStateFlow()
    
    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()
    
    init {
        loadFilters()
        loadSavedFilters()
    }
    
    private fun loadSavedFilters() {
        viewModelScope.launch {
            repository.getSavedFilters().collect { savedFilters ->
                loadSavedFilters(savedFilters)
            }
        }
    }
    
    private fun loadFilters() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val platformsResult = repository.getPlatforms()
                val genresResult = repository.getGenres()
                
                platformsResult.fold(
                    onSuccess = { response ->
                        _platforms.value = response.results
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            error = "Error al cargar plataformas: ${exception.message}"
                        )
                    }
                )
                
                genresResult.fold(
                    onSuccess = { response ->
                        _genres.value = response.results
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            error = "Error al cargar géneros: ${exception.message}"
                        )
                    }
                )
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
    
    fun togglePlatform(platformId: String) {
        val currentList = _selectedPlatforms.value.toMutableList()
        if (currentList.contains(platformId)) {
            currentList.remove(platformId)
        } else {
            currentList.add(platformId)
        }
        _selectedPlatforms.value = currentList
    }
    
    fun toggleGenre(genreId: String) {
        val currentList = _selectedGenres.value.toMutableList()
        if (currentList.contains(genreId)) {
            currentList.remove(genreId)
        } else {
            currentList.add(genreId)
        }
        _selectedGenres.value = currentList
    }
    
    fun setSortOption(sortOption: SortOption?) {
        _selectedSortOption.value = sortOption
    }
    
    fun applyFilters(): GameFilters {
        return GameFilters(
            platforms = _selectedPlatforms.value,
            genres = _selectedGenres.value,
            ordering = _selectedSortOption.value?.apiValue
        )
    }
    
    fun clearFilters() {
        _selectedPlatforms.value = emptyList()
        _selectedGenres.value = emptyList()
        _selectedSortOption.value = null
    }
    
    fun loadSavedFilters(filters: GameFilters) {
        _selectedPlatforms.value = filters.platforms
        _selectedGenres.value = filters.genres
        _selectedSortOption.value = SortOption.values().find { it.apiValue == filters.ordering }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun retryLoading() {
        loadFilters()
    }
}

data class FiltersUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
