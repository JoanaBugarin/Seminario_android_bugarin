package com.seminario.videojuegosapp.data.repository

import com.seminario.videojuegosapp.data.api.RawgApiService
import com.seminario.videojuegosapp.data.local.WishlistDao
import com.seminario.videojuegosapp.data.model.*
import com.seminario.videojuegosapp.data.preferences.FiltersPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val apiService: RawgApiService,
    private val wishlistDao: WishlistDao,
    private val filtersPreferences: FiltersPreferences
) {
    
    suspend fun getGames(
        page: Int = 1,
        pageSize: Int = 20,
        search: String? = null,
        platforms: String? = null,
        genres: String? = null,
        ordering: String? = null
    ): Result<GameResponse> {
        return try {
            val response = apiService.getGames(page, pageSize, search, platforms, genres, ordering)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getGameDetails(id: Int): Result<Game> {
        return try {
            val response = apiService.getGameDetails(id)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPlatforms(page: Int = 1, pageSize: Int = 20): Result<PlatformResponse> {
        return try {
            val response = apiService.getPlatforms(page, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getGenres(page: Int = 1, pageSize: Int = 20): Result<GenreResponse> {
        return try {
            val response = apiService.getGenres(page, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Wishlist methods
    fun getAllWishlistGames(): Flow<List<WishlistGame>> {
        return wishlistDao.getAllWishlistGames()
    }
    
    suspend fun addToWishlist(game: Game) {
        val wishlistGame = WishlistGame(
            id = game.id,
            name = game.name,
            background_image = game.background_image,
            released = game.released,
            rating = game.rating
        )
        wishlistDao.insertWishlistGame(wishlistGame)
    }
    
    suspend fun removeFromWishlist(gameId: Int) {
        wishlistDao.deleteWishlistGameById(gameId)
    }
    
    suspend fun isGameInWishlist(gameId: Int): Boolean {
        return wishlistDao.isGameInWishlist(gameId)
    }
    
    suspend fun getWishlistGameById(gameId: Int): WishlistGame? {
        return wishlistDao.getWishlistGameById(gameId)
    }
    
    // Filters preferences methods
    fun getSavedFilters(): Flow<GameFilters> {
        return filtersPreferences.filters
    }
    
    suspend fun saveFilters(filters: GameFilters) {
        filtersPreferences.saveFilters(filters)
    }
    
    suspend fun clearSavedFilters() {
        filtersPreferences.clearFilters()
    }
}
