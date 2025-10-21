package com.seminario.videojuegosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.seminario.videojuegosapp.data.model.Game
import com.seminario.videojuegosapp.data.model.GameFilters
import com.seminario.videojuegosapp.data.paging.GamePagingSource
import com.seminario.videojuegosapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _gamesUiState = MutableStateFlow(GamesUiState())
    val gamesUiState: StateFlow<GamesUiState> = _gamesUiState.asStateFlow()
    
    private val _filters = MutableStateFlow(GameFilters())
    val filters: StateFlow<GameFilters> = _filters.asStateFlow()
    
    init {
        loadSavedFilters()
    }
    
    private fun loadSavedFilters() {
        viewModelScope.launch {
            repository.getSavedFilters().collect { savedFilters ->
                _filters.value = savedFilters
            }
        }
    }
    
    val games: Flow<PagingData<Game>> = combine(
        searchQuery,
        filters
    ) { query: String, filters: GameFilters ->
        if (query.isNotBlank()) {
            // Si hay búsqueda, ignoramos los filtros
            GamePagingSource(repository, searchQuery = query)
        } else {
            // Si no hay búsqueda, aplicamos los filtros
            GamePagingSource(repository, filters = filters)
        }
    }.flatMapLatest { pagingSource: GamePagingSource ->
        Pager<Int, Game>(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { pagingSource }
        ).flow
    }.cachedIn(viewModelScope)
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _isSearching.value = query.isNotBlank()
    }
    
    fun updateFilters(filters: GameFilters) {
        _filters.value = filters
        viewModelScope.launch {
            repository.saveFilters(filters)
        }
    }
    
    fun clearFilters() {
        _filters.value = GameFilters()
        viewModelScope.launch {
            repository.clearSavedFilters()
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _isSearching.value = false
    }
    
    fun refreshGames() {
        viewModelScope.launch {
            _gamesUiState.value = _gamesUiState.value.copy(isLoading = true, error = null)
            try {
                // La paginación se encarga del refresh automáticamente
                _gamesUiState.value = _gamesUiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _gamesUiState.value = _gamesUiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
    
    fun clearError() {
        _gamesUiState.value = _gamesUiState.value.copy(error = null)
    }
}

data class GamesUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
