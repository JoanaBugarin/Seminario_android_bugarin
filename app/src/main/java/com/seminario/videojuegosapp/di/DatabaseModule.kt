package com.seminario.videojuegosapp.di

import android.content.Context
import com.seminario.videojuegosapp.data.local.VideojuegosDatabase
import com.seminario.videojuegosapp.data.local.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideVideojuegosDatabase(
        @ApplicationContext context: Context
    ): VideojuegosDatabase {
        return VideojuegosDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideWishlistDao(database: VideojuegosDatabase): WishlistDao {
        return database.wishlistDao()
    }
}





