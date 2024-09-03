package com.example.jokesapp.repository

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JokesRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val jokesDao: JokesDao
) {
    suspend fun fetchUnbookmarkedJokes(
        genre: String,
        amount: Int
    ): Flow<List<JokesEntity>> {
        try {
            val response = apiService.fetchAllJokes(genre = genre, amount = amount)
            val jokesResponse = response.body()
            if (response.isSuccessful && jokesResponse != null) {
                val jokesEntityList = jokesResponse.jokes.map { joke ->
                    JokesEntity(
                        id = joke.id,
                        type = joke.type,
                        setup = joke.setup,
                        punchLine = joke.delivery,
                        jokeMessage = joke.joke
                    )
                }
                jokesDao.insertJokesList(jokesEntityList)
            }
        } catch (e: Exception) {
        }
        return jokesDao.fetchUnbookmarkedJokes()
    }

    suspend fun updateBookMarkStatus(id: Int, bookmarked: Boolean) {
        jokesDao.updateBookmarkStatus(jokeId = id, isBookmarked = bookmarked)
    }

    fun fetchBookmarkedJokes(): Flow<List<JokesEntity>> {
        return jokesDao.fetchBookmarkedJokes()
    }

    suspend fun deleteUnbookmarkedJokes() {
        jokesDao.deleteUnbookmarkedJokes()
    }

    suspend fun deleteJokeViaId(id: Int) {
        jokesDao.deleteJokeViaId(id)
    }
}