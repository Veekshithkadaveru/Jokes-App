package com.example.jokesapp.repository

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.IOException
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
        val cachedJokesFlow = jokesDao.fetchUnbookmarkedJokes()

        return cachedJokesFlow.onStart {
            val cachedJokes = cachedJokesFlow.firstOrNull()
            if (cachedJokes.isNullOrEmpty()) {
                try {
                    val response = apiService.fetchAllJokes(genre = genre, amount = amount)

                    if (response.isSuccessful) {
                        val jokesResponse = response.body()
                        if (jokesResponse != null) {
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
                        } else {
                            throw IllegalStateException("Response body is null despite successful response")
                        }
                    } else {

                        throw HttpException(response)
                    }
                } catch (e: HttpException) {
                    println("HTTP Error: ${e.message()}, Code: ${e.code()}")
                    throw e
                } catch (e: IOException) {
                    println("Network Error: ${e.localizedMessage}")
                    throw IOException("Network issue or server unreachable: ${e.localizedMessage}")
                } catch (e: Exception) {
                    println("Unknown Error: ${e.localizedMessage}")
                    throw Exception("An unexpected error occurred: ${e.localizedMessage}")
                }
            }
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
