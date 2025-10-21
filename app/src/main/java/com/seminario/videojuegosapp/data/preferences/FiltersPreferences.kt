package com.seminario.videojuegosapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.seminario.videojuegosapp.data.model.GameFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filters_preferences")

@Singleton
class FiltersPreferences @Inject constructor(
    private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore
    
    companion object {
        private val PLATFORMS_KEY = stringSetPreferencesKey("platforms")
        private val GENRES_KEY = stringSetPreferencesKey("genres")
        private val ORDERING_KEY = stringPreferencesKey("ordering")
    }
    
    val filters: Flow<GameFilters> = dataStore.data.map { preferences ->
        GameFilters(
            platforms = preferences[PLATFORMS_KEY]?.toList() ?: emptyList(),
            genres = preferences[GENRES_KEY]?.toList() ?: emptyList(),
            ordering = preferences[ORDERING_KEY]
        )
    }
    
    suspend fun saveFilters(filters: GameFilters) {
        dataStore.edit { preferences ->
            preferences[PLATFORMS_KEY] = filters.platforms.toSet()
            preferences[GENRES_KEY] = filters.genres.toSet()
            preferences[ORDERING_KEY] = filters.ordering as String
        }
    }
    
    suspend fun clearFilters() {
        dataStore.edit { preferences ->
            preferences.remove(PLATFORMS_KEY)
            preferences.remove(GENRES_KEY)
            preferences.remove(ORDERING_KEY)
        }
    }
}


