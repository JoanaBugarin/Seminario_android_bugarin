package com.seminario.videojuegosapp.data.api

import com.seminario.videojuegosapp.data.model.Game
import com.seminario.videojuegosapp.data.model.GameResponse
import com.seminario.videojuegosapp.data.model.GenreResponse
import com.seminario.videojuegosapp.data.model.PlatformResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {
    
    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("search") search: String? = null,
        @Query("platforms") platforms: String? = null,
        @Query("genres") genres: String? = null,
        @Query("ordering") ordering: String? = null
    ): Response<GameResponse>
    
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int
    ): Response<Game>
    
    @GET("platforms")
    suspend fun getPlatforms(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<PlatformResponse>
    
    @GET("genres")
    suspend fun getGenres(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<GenreResponse>
}
