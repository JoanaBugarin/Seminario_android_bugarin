package com.seminario.videojuegosapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.seminario.videojuegosapp.data.model.WishlistGame

@Database(
    entities = [WishlistGame::class],
    version = 1,
    exportSchema = false
)
abstract class VideojuegosDatabase : RoomDatabase() {
    
    abstract fun wishlistDao(): WishlistDao
    
    companion object {
        @Volatile
        private var INSTANCE: VideojuegosDatabase? = null
        
        fun getDatabase(context: Context): VideojuegosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideojuegosDatabase::class.java,
                    "videojuegos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


