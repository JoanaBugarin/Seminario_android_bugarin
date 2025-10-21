package com.seminario.videojuegosapp.data.local

import androidx.room.*
import com.seminario.videojuegosapp.data.model.WishlistGame
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    
    @Query("SELECT * FROM wishlist_games ORDER BY name ASC")
    fun getAllWishlistGames(): Flow<List<WishlistGame>>
    
    @Query("SELECT * FROM wishlist_games WHERE id = :gameId")
    suspend fun getWishlistGameById(gameId: Int): WishlistGame?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistGame(game: WishlistGame)
    
    @Delete
    suspend fun deleteWishlistGame(game: WishlistGame)
    
    @Query("DELETE FROM wishlist_games WHERE id = :gameId")
    suspend fun deleteWishlistGameById(gameId: Int)
    
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_games WHERE id = :gameId)")
    suspend fun isGameInWishlist(gameId: Int): Boolean
}


