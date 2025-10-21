package com.seminario.videojuegosapp.ui.screens.games

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
import com.seminario.videojuegosapp.ui.components.GameCard
import com.seminario.videojuegosapp.ui.components.LoadingIndicator
import com.seminario.videojuegosapp.ui.viewmodel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onNavigateToFilters: () -> Unit,
    onNavigateToGameDetails: (Int) -> Unit,
    viewModel: GamesViewModel = hiltViewModel()
) {
    val games = viewModel.games.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val uiState by viewModel.gamesUiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (isSearching) {
                        stringResource(R.string.search_hint)
                    } else {
                        stringResource(R.string.games_list_title)
                    }
                )
            },
            actions = {
                IconButton(onClick = onNavigateToFilters) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(R.string.filter_title)
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            placeholder = {
                Text(stringResource(R.string.search_hint))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            
            uiState.error != null -> {
                val errorMessage = uiState.error!!
                ErrorScreen(
                    error = errorMessage,
                    onRetry = viewModel::refreshGames
                )
            }
            
            else -> {
                GamesList(
                    games = games,
                    onGameClick = onNavigateToGameDetails,
                    isLandscape = isLandscape,
                    isSearching = isSearching
                )
            }
        }
    }
}

@Composable
fun GamesList(
    games: LazyPagingItems<Game>,
    onGameClick: (Int) -> Unit,
    isLandscape: Boolean,
    isSearching: Boolean
) {
    when {
        games.itemCount == 0 && games.loadState.refresh is androidx.paging.LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
        
        games.itemCount == 0 && games.loadState.refresh is androidx.paging.LoadState.NotLoading -> {
            EmptyState(isSearching = isSearching)
        }
        
        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(games.itemCount) { index ->
                    games[index]?.let { game ->
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                            isLandscape = isLandscape
                        )
                    }
                }
                
                item {
                    if (games.loadState.append is androidx.paging.LoadState.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(isSearching: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (isSearching) {
                    stringResource(R.string.no_search_results)
                } else {
                    stringResource(R.string.no_games_found)
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}


