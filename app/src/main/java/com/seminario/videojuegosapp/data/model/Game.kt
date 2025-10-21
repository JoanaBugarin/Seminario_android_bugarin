package com.seminario.videojuegosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Game(
    val id: Int,
    val name: String,
    val background_image: String?,
    val released: String?,
    val rating: Double?,
    val rating_top: Int?,
    val platforms: List<Platform>?,
    val genres: List<Genre>?,
    val short_screenshots: List<ShortScreenshot>?,
    val description_raw: String?,
    val website: String?,
    val metacritic: Int?
)

data class GameResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

data class Platform(
    val platform: PlatformInfo
)

data class PlatformInfo(
    val id: Int,
    val name: String,
    val slug: String
)

data class Genre(
    val id: Int,
    val name: String,
    val slug: String
)

data class ShortScreenshot(
    val id: Int,
    val image: String
)

@Entity(tableName = "wishlist_games")
data class WishlistGame(
    @PrimaryKey val id: Int,
    val name: String,
    val background_image: String?,
    val released: String?,
    val rating: Double?
)

data class PlatformResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PlatformInfo>
)

data class GenreResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Genre>
)


