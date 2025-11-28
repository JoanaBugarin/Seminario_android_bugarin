package com.seminario.videojuegosapp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.seminario.videojuegosapp.R
import com.seminario.videojuegosapp.data.model.Game
import com.seminario.videojuegosapp.data.model.WishlistGame
import com.seminario.videojuegosapp.ui.components.GameCard
import com.seminario.videojuegosapp.ui.components.LoadingIndicator
import com.seminario.videojuegosapp.ui.screens.games.GamesScreen
import com.seminario.videojuegosapp.ui.screens.wishlist.WishlistScreen
import com.seminario.videojuegosapp.ui.viewmodel.GamesViewModel
import com.seminario.videojuegosapp.ui.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToFilters: () -> Unit,
    onNavigateToGameDetails: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val gamesViewModel: GamesViewModel = hiltViewModel()
    val wishlistViewModel: WishlistViewModel = hiltViewModel()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = when (selectedTab) {
                        0 -> stringResource(R.string.games_list_title)
                        1 -> stringResource(R.string.wishlist_title)
                        else -> stringResource(R.string.app_name)
                    }
                )
            },
            actions = {
                if (selectedTab == 0) {
                    IconButton(onClick = onNavigateToFilters) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.filter_title)
                        )
                    }
                }
            }
        )
        
        // Tab Row
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(R.string.games_list_title)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.wishlist_title)) }
            )
        }
        
        // Content
        when (selectedTab) {
            0 -> {
                GamesScreen(
                    onNavigateToFilters = onNavigateToFilters,
                    onNavigateToGameDetails = onNavigateToGameDetails,
                    viewModel = gamesViewModel
                )
            }
            1 -> {
                WishlistScreen(
                    onNavigateToGameDetails = onNavigateToGameDetails,
                    viewModel = wishlistViewModel
                )
            }
        }
    }
}





