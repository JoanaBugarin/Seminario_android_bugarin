package com.seminario.videojuegosapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.seminario.videojuegosapp.data.model.Game
import com.seminario.videojuegosapp.data.model.GameFilters
import com.seminario.videojuegosapp.data.repository.GameRepository

class GamePagingSource(
    private val repository: GameRepository,
    private val searchQuery: String? = null,
    private val filters: GameFilters = GameFilters()
) : PagingSource<Int, Game>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            
            val result = if (searchQuery != null && searchQuery.isNotBlank()) {
                repository.getGames(
                    page = page,
                    pageSize = pageSize,
                    search = searchQuery
                )
            } else {
                repository.getGames(
                    page = page,
                    pageSize = pageSize,
                    platforms = filters.toQueryMap()["platforms"],
                    genres = filters.toQueryMap()["genres"],
                    ordering = filters.toQueryMap()["ordering"]
                )
            }
            
            result.fold(
                onSuccess = { response ->
                    LoadResult.Page(
                        data = response.results,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (response.next == null) null else page + 1
                    )
                },
                onFailure = { exception ->
                    LoadResult.Error(exception)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}





