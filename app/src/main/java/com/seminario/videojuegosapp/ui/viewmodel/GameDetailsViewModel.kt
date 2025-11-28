package com.seminario.videojuegosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seminario.videojuegosapp.data.model.Game
import com.seminario.videojuegosapp.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {
    
    private val _gameDetails = MutableStateFlow<Game?>(null)
    val gameDetails: StateFlow<Game?> = _gameDetails.asStateFlow()
    
    private val _isInWishlist = MutableStateFlow(false)
    val isInWishlist: StateFlow<Boolean> = _isInWishlist.asStateFlow()
    
    private val _uiState = MutableStateFlow(GameDetailsUiState())
    val uiState: StateFlow<GameDetailsUiState> = _uiState.asStateFlow()
    
    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = repository.getGameDetails(gameId)
                result.fold(
                    onSuccess = { game ->
                        _gameDetails.value = game
                        checkIfInWishlist(gameId)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al cargar los detalles del juego"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
    
    private fun checkIfInWishlist(gameId: Int) {
        viewModelScope.launch {
            try {
                val isInWishlist = repository.isGameInWishlist(gameId)
                _isInWishlist.value = isInWishlist
            } catch (e: Exception) {
                // Error silencioso para no interrumpir la carga de detalles
            }
        }
    }
    
    fun toggleWishlist() {
        viewModelScope.launch {
            try {
                val game = _gameDetails.value
                if (game != null) {
                    if (_isInWishlist.value) {
                        repository.removeFromWishlist(game.id)
                        _isInWishlist.value = false
                    } else {
                        repository.addToWishlist(game)
                        _isInWishlist.value = true
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al actualizar la lista de deseados"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class GameDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)





