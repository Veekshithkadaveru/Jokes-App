package com.example.jokesapp.fakes

import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.model.JokeOrSetUp
import com.example.jokesapp.repository.JokesRepoImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FakesJokesRepoImpl {

    private lateinit var jokesRepo: JokesRepoImpl
    private lateinit var fakeJokesDao: FakesJokesDao
    private lateinit var fakeApiService: FakeApiService

    @Before
    fun setUp() {
        fakeApiService = FakeApiService()
        fakeJokesDao = FakesJokesDao()
        jokesRepo = JokesRepoImpl(apiService = FakeApiService(), jokesDao = FakesJokesDao())
    }

    @Test
    fun `fetchBookmarked Jokes should return only bookmarked Jokes`() = runBlocking {

        val joke1 = JokesEntity(
            id = 1,
            type = "general",
            setup = "Setup 1",
            punchLine = "Punchline 1",
            jokeMessage = null,
            isBookMarked = true
        )
        val joke2 = JokesEntity(
            id = 2,
            type = "general",
            setup = "Setup 2",
            punchLine = "Punchline 2",
            jokeMessage = null,
            isBookMarked = false
        )

        fakeJokesDao.insertJokesList(listOf(joke1, joke2))

        val result = fakeJokesDao.fetchBookmarkedJokes().first()

        assertEquals(1, result.size)
        assertEquals("Setup 1", result[0].setup)

    }
}