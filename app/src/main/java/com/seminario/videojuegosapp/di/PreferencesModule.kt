package com.seminario.videojuegosapp.di

import android.content.Context
import com.seminario.videojuegosapp.data.preferences.FiltersPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    
    @Provides
    @Singleton
    fun provideFiltersPreferences(
        @ApplicationContext context: Context
    ): FiltersPreferences {
        return FiltersPreferences(context)
    }
}


