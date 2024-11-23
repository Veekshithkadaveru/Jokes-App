package com.example.jokesapp

import com.example.jokesapp.data.local.JokesDao
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.data.model.JokeOrSetUp
import com.example.jokesapp.data.model.JokesResponse
import com.example.jokesapp.data.remote.ApiService
import com.example.jokesapp.repository.JokesRepoImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class JokesRepoImplTest {

    private lateinit var jokesRepo: JokesRepoImpl
    private lateinit var apiService: ApiService
    private lateinit var jokesDao: JokesDao

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        jokesDao = mockk(relaxed = true)
        jokesRepo = JokesRepoImpl(apiService, jokesDao)
    }

    @Test
    fun `fetchUnBookMarkedJokes calls ApiService fetchAllJokes`() = runTest {

        val mockResponse = JokesResponse(
            jokes = listOf(
                JokeOrSetUp(
                    id = 1, type = "type1", setup = "setup1", delivery = "delivery1", joke = null
                ),
                JokeOrSetUp(
                    id = 2, type = "type2", setup = "setup2", delivery = "delivery2", joke = null
                )
            )
        )
        coEvery { apiService.fetchAllJokes("Any", 10) } returns Response.success(mockResponse)
        coEvery { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        jokesRepo.fetchUnBookMarkedJokes("Any", 10).first()
        coVerify { apiService.fetchAllJokes("Any", 10) }
    }

    @Test
    fun `fetchUnBookMarkedJokes saves jokes to database when response is successful`() = runTest {

        val mockResponse = JokesResponse(
            jokes = listOf(
                JokeOrSetUp(
                    id = 1, type = "type1", setup = "setup1", delivery = "delivery1", joke = null
                ),
                JokeOrSetUp(
                    id = 2, type = "type2", setup = "setup2", delivery = "delivery2", joke = null
                )
            )
        )

        coEvery { apiService.fetchAllJokes("Any", 10) } returns Response.success(mockResponse)
        coEvery { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        jokesRepo.fetchUnBookMarkedJokes("Any", 10).first()
        coVerify {
            jokesDao.insertJokesList(
                listOf(
                    JokesEntity(
                        id = 1,
                        type = "type1",
                        setup = "setup1",
                        punchLine = "delivery1",
                        jokeMessage = null
                    ),
                    JokesEntity(
                        id = 2,
                        type = "type2",
                        setup = "setup2",
                        punchLine = "delivery2",
                        jokeMessage = null
                    )
                )
            )
        }
    }

    @Test
    fun `fetchUnBookMarkedJokes handles empty API response`() = runTest {

        coEvery {
            apiService.fetchAllJokes("Any", 10)
        } returns Response.success(JokesResponse(jokes = emptyList()))

        coEvery { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        val result = jokesRepo.fetchUnBookMarkedJokes("Any", 10).first()

        assertEquals(emptyList<JokesEntity>(), result)
        coVerify(exactly = 0) { jokesDao.insertJokesList(any()) }
    }

    @Test
    fun `fetchUnBookMarkedJokes returns unbookmarked jokes from database`() = runTest {

        val mockJokes = listOf(
            JokesEntity(
                id = 1,
                type = "type1",
                setup = "setup1",
                punchLine = "delivery1",
                jokeMessage = null
            ),
            JokesEntity(
                id = 2,
                type = "type2",
                setup = "setup2",
                punchLine = "delivery2",
                jokeMessage = null
            )
        )
        coEvery { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(mockJokes)

        val result = jokesRepo.fetchUnBookMarkedJokes("Any", 10).first()

        assertEquals(mockJokes, result)
    }

    @Test
    fun `fetchUnBookMarkedJokes handles API error without crashing`() = runTest {

        coEvery { apiService.fetchAllJokes("Any", 10) } throws Exception("Network Error")
        coEvery { jokesDao.fetchUnbookmarkedJokes() } returns flowOf(emptyList())

        val result = jokesRepo.fetchUnBookMarkedJokes("Any", 10).first()

        assertEquals(emptyList<JokesEntity>(), result)
        coVerify(exactly = 0) { jokesDao.insertJokesList(any()) }
    }

}