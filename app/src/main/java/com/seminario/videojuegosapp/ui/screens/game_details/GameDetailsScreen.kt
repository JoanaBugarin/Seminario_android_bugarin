package com.seminario.videojuegosapp.ui.screens.game_details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.seminario.videojuegosapp.R
import com.seminario.videojuegosapp.ui.components.LoadingIndicator
import com.seminario.videojuegosapp.ui.viewmodel.GameDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    gameId: Int,
    onNavigateBack: () -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    val gameDetails by viewModel.gameDetails.collectAsState()
    val isInWishlist by viewModel.isInWishlist.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = gameDetails?.name ?: stringResource(R.string.game_details_title)
                )
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
                IconButton(
                    onClick = {
                        viewModel.toggleWishlist()
                    }
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isInWishlist) {
                            stringResource(R.string.remove_from_wishlist)
                        } else {
                            stringResource(R.string.add_to_wishlist)
                        }
                    )
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
                    onRetry = { viewModel.loadGameDetails(gameId) }
                )
            }
            
            gameDetails != null -> {
                GameDetailsContent(
                    game = gameDetails!!,
                    isInWishlist = isInWishlist,
                    onToggleWishlist = { viewModel.toggleWishlist() },
                    onShare = { shareGame(context, gameDetails!!) },
                    onOpenWebsite = { openWebsite(context, gameDetails!!.website) }
                )
            }
        }
    }
}

@Composable
fun GameDetailsContent(
    game: com.seminario.videojuegosapp.data.model.Game,
    isInWishlist: Boolean,
    onToggleWishlist: () -> Unit,
    onShare: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Game Image
        AsyncImage(
            model = game.background_image,
            contentDescription = game.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        
        // Game Title
        Text(
            text = game.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // Basic Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            game.released?.let { released ->
                Column {
                    Text(
                        text = stringResource(R.string.released),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = released,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            game.rating?.let { rating ->
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.rating),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        // Platforms
        game.platforms?.let { platforms ->
            Column {
                Text(
                    text = stringResource(R.string.platforms),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = platforms.joinToString(", ") { it.platform.name },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Genres
        game.genres?.let { genres ->
            Column {
                Text(
                    text = stringResource(R.string.genres),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = genres.joinToString(", ") { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Description
        Column {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = game.description_raw ?: stringResource(R.string.no_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onToggleWishlist,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isInWishlist) {
                        stringResource(R.string.remove_from_wishlist)
                    } else {
                        stringResource(R.string.add_to_wishlist)
                    }
                )
            }
            
            OutlinedButton(
                onClick = onShare,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.share_game))
            }
        }
        
        // Website Button
        game.website?.let { website ->
            if (website.isNotBlank()) {
                Button(
                    onClick = onOpenWebsite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.open_website))
                }
            }
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

private fun shareGame(context: android.content.Context, game: com.seminario.videojuegosapp.data.model.Game) {
    val shareText = buildString {
        append("¡Mira este videojuego!\n\n")
        append("${game.name}\n")
        game.description_raw?.let { description ->
            append("$description\n\n")
        }
        append("¡Espero que te guste!")
    }
    
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    
    context.startActivity(Intent.createChooser(shareIntent, "Compartir juego"))
}

private fun openWebsite(context: android.content.Context, website: String?) {
    website?.let { url ->
        if (url.isNotBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                // Manejar error silenciosamente
            }
        }
    }
}
