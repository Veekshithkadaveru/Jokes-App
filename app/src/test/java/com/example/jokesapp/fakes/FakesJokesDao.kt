package com.example.jokesapp.fakes

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakesJokesDao : JokesDao {
    private val jokesList = mutableListOf<JokesEntity>()
    override fun fetchAllJokes(): Flow<List<JokesEntity>> {
        return flowOf(jokesList)
    }

    override fun fetchBookmarkedJokes(): Flow<List<JokesEntity>> {
        return flowOf(jokesList.filter { it.isBookMarked })
    }

    override suspend fun updateBookmarkStatus(jokeId: Int, isBookmarked: Boolean) {
        jokesList.find { it.id == jokeId }?.isBookMarked = isBookmarked
    }

    override fun fetchUnbookmarkedJokes(): Flow<List<JokesEntity>> {
        return flowOf(jokesList.filter { !it.isBookMarked })
    }

    override suspend fun insertJoke(jokesEntity: JokesEntity) {
        jokesList.add(jokesEntity)
    }

    override suspend fun insertJokesList(jokesEntity: List<JokesEntity>) {
        jokesList.addAll(jokesEntity)
    }

    override suspend fun deleteJokeViaId(id: Int) {
        jokesList.removeIf { it.id == id }
    }

    override suspend fun deleteUnbookmarkedJokes() {
        jokesList.removeIf { !it.isBookMarked }
    }
}