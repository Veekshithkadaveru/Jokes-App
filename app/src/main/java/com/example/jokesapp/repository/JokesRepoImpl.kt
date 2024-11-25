package com.example.jokesapp.repository

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class JokesRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val jokesDao: JokesDao
) : JokesRepo {
    override fun fetchBookMarkedJokes(): Flow<List<JokesEntity>> {

        return jokesDao.fetchBookmarkedJokes()
    }

    override suspend fun fetchUnBookMarkedJokes(
        genre: String,
        amount: Int
    ): Flow<List<JokesEntity>> {

        val cachedJokes = jokesDao.fetchUnbookmarkedJokes().firstOrNull()
        return if (cachedJokes.isNullOrEmpty()) {

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
                throw Exception("Failed to fetch jokes from API, You can now only view Bookmarked Jokes")
            }
            return jokesDao.fetchUnbookmarkedJokes()
        } else {
            jokesDao.fetchUnbookmarkedJokes()
        }
    }

    override suspend fun updateBookmarkStatus(id: Int, bookmarked: Boolean) {
        jokesDao.updateBookmarkStatus(jokeId = id, isBookmarked = bookmarked)
    }

    override suspend fun deleteUnbookmarkedJokes() {
        jokesDao.deleteUnbookmarkedJokes()
    }

    override suspend fun deleteJokeViaId(id: Int) {
        jokesDao.deleteJokeViaId(id)
    }

}
