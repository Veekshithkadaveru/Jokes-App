package com.example.jokesapp

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.model.JokeOrSetUp
import com.example.jokesapp.data.model.JokesResponse
import com.example.jokesapp.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response




class JokesRepoImpl {

    private lateinit var jokesRepo: JokesRepoImpl
    private lateinit var apiService: ApiService
    private lateinit var jokesDao: JokesDao

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        jokesDao = mockk(relaxed = true)
        jokesRepo = JokesRepoImpl()
    }

    @Test
    fun `fetchUnBookMarkedJokes calls ApiService fetchAllJokes`() = runTest {
        // Mock API response
        val mockResponse = JokesResponse(
            jokes = listOf(
                JokeOrSetUp(id = 1, type = "type1", setup = "setup1", delivery = "delivery1", joke = null)
                
            )
        )
        coEvery { apiService.fetchAllJokes("Any", 10) } returns Response.success(mockResponse)

        // Mock DAO behavior
        every { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        // Call the repository function
        jokesRepo.fetchUnBookMarkedJokes("Any", 10)

        // Verify ApiService's fetchAllJokes was called
        coVerify { apiService.fetchAllJokes("Any", 10) }
    }

    @Test
    fun `fetchUnBookMarkedJokes saves jokes to database when response is successful`() = runTest {
        // Mock API response
        val mockResponse = JokesResponse(
            jokes = listOf(
                JokesResponse.Joke(id = 1, type = "type1", setup = "setup1", delivery = "delivery1", joke = null),
                JokesResponse.Joke(id = 2, type = "type2", setup = "setup2", delivery = "delivery2", joke = null),
            )
        )
        coEvery { apiService.fetchAllJokes("Any", 10) } returns Response.success(mockResponse)

        // Mock DAO behavior
        every { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        // Call the repository function
        jokesRepo.fetchUnBookMarkedJokes("Any", 10)

        // Verify jokes were inserted into the database
        coVerify {
            jokesDao.insertJokesList(
                listOf(
                    JokesEntity(id = 1, type = "type1", setup = "setup1", punchLine = "delivery1", jokeMessage = null),
                    JokesEntity(id = 2, type = "type2", setup = "setup2", punchLine = "delivery2", jokeMessage = null)
                )
            )
        }
    }
}