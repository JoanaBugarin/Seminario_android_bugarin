package com.seminario.videojuegosapp.ui.screens.filters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seminario.videojuegosapp.R
import com.seminario.videojuegosapp.data.model.SortOption
import com.seminario.videojuegosapp.ui.components.LoadingIndicator
import com.seminario.videojuegosapp.ui.viewmodel.FiltersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onNavigateBack: () -> Unit,
    viewModel: FiltersViewModel = hiltViewModel()
) {
    val platforms by viewModel.platforms.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val selectedPlatforms by viewModel.selectedPlatforms.collectAsState()
    val selectedGenres by viewModel.selectedGenres.collectAsState()
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(stringResource(R.string.filter_title))
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = {
                        viewModel.clearFilters()
                    }
                ) {
                    Text(stringResource(R.string.clear_filters))
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
                ErrorScreen(
                    error = uiState.error!!,
                    onRetry = viewModel::retryLoading
                )
            }
            
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.sort_by),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    item {
                        SortOptionsSection(
                            selectedOption = selectedSortOption,
                            onOptionSelected = viewModel::setSortOption
                        )
                    }
                    
                    item {
                        Divider()
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.platform_filter),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    item {
                        FilterChipsSection(
                            items = platforms.map { it.id.toString() to it.name },
                            selectedItems = selectedPlatforms,
                            onItemToggle = viewModel::togglePlatform
                        )
                    }
                    
                    item {
                        Divider()
                    }
                    
                    item {
                        Text(
                            text = stringResource(R.string.genre_filter),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    item {
                        FilterChipsSection(
                            items = genres.map { it.id.toString() to it.name },
                            selectedItems = selectedGenres,
                            onItemToggle = viewModel::toggleGenre
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    
                    item {
                        Button(
                            onClick = {
                                viewModel.saveCurrentFilters()
                                onNavigateBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.apply_filters))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SortOptionsSection(
    selectedOption: SortOption?,
    onOptionSelected: (SortOption?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SortOption.values().forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = option.displayName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterChipsSection(
    items: List<Pair<String, String>>,
    selectedItems: List<String>,
    onItemToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (id, label) ->
            FilterChip(
                selected = selectedItems.contains(id),
                onClick = { onItemToggle(id) },
                label = {
                    Text(
                        text = label,
                        maxLines = 1
                    )
                }
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
                style = MaterialTheme.typography.bodyLarge
            )
            
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}




