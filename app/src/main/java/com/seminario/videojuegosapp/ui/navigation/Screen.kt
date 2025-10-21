package com.seminario.videojuegosapp.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object GamesList : Screen("games_list")
    object Filters : Screen("filters")
    object GameDetails : Screen(
        route = "game_details/{gameId}",
        arguments = listOf(
            navArgument("gameId") {
                type = NavType.IntType
            }
        )
    ) {
        fun createRoute(gameId: Int) = "game_details/$gameId"
    }
    object Wishlist : Screen("wishlist")
}


