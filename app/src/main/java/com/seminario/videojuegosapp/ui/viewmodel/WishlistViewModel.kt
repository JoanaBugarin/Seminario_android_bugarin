package com.seminario.videojuegosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seminario.videojuegosapp.data.model.WishlistGame
import com.seminario.videojuegosapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {
    
    val wishlistGames: Flow<List<WishlistGame>> = repository.getAllWishlistGames()
    
    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()
    
    fun removeFromWishlist(gameId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFromWishlist(gameId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al quitar de la lista de deseados"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class WishlistUiState(
    val error: String? = null
)





