package com.example.jokesapp.repository

import com.example.jokesapp.data.local.JokesEntity
import kotlinx.coroutines.flow.Flow

interface JokesRepo {
    fun fetchBookMarkedJokes(): Flow<List<JokesEntity>>
    suspend fun fetchUnBookMarkedJokes(genre: String, amount: Int): Flow<List<JokesEntity>>
    suspend fun updateBookmarkStatus(id: Int, bookmarked: Boolean)
    suspend fun deleteUnbookmarkedJokes()
    suspend fun deleteJokeViaId(id: Int)
}