package com.seminario.videojuegosapp.data.model

data class GameFilters(
    val platforms: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val ordering: String? = null
) {
    fun hasFilters(): Boolean {
        return platforms.isNotEmpty() || genres.isNotEmpty() || ordering != null
    }
    
    fun toQueryMap(): Map<String, String> {
        val queryMap = mutableMapOf<String, String>()
        
        if (platforms.isNotEmpty()) {
            queryMap["platforms"] = platforms.joinToString(",")
        }
        
        if (genres.isNotEmpty()) {
            queryMap["genres"] = genres.joinToString(",")
        }
        
        ordering?.let { queryMap["ordering"] = it }
        
        return queryMap
    }
}

enum class SortOption(val displayName: String, val apiValue: String) {
    NAME("Nombre", "name"),
    RELEASED_DATE("Fecha de lanzamiento", "-released"),
    RATING("Rating", "-rating"),
    METACRITIC("Metacritic", "-metacritic")
}





