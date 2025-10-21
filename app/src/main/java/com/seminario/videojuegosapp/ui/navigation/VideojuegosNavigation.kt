package com.seminario.videojuegosapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seminario.videojuegosapp.ui.screens.main.MainScreen
import com.seminario.videojuegosapp.ui.screens.filters.FiltersScreen
import com.seminario.videojuegosapp.ui.screens.game_details.GameDetailsScreen

@Composable
fun VideojuegosNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.GamesList.route,
        modifier = modifier
    ) {
        composable(Screen.GamesList.route) {
            MainScreen(
                onNavigateToFilters = {
                    navController.navigate(Screen.Filters.route)
                },
                onNavigateToGameDetails = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                }
            )
        }
        
        composable(Screen.Filters.route) {
            FiltersScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.GameDetails.route,
            arguments = Screen.GameDetails.arguments
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
            GameDetailsScreen(
                gameId = gameId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
    }
}
