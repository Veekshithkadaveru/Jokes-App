package com.example.jokesapp.fakes

import com.example.jokesapp.data.model.JokeOrSetUp
import com.example.jokesapp.data.model.JokesResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class FakeApiServiceTest {

    @Test
    fun `fetchAllJokes returns mocked jokes`() = runBlocking {

        val fakeApiService = FakeApiService()

        val mockedJokes = listOf(
            JokeOrSetUp(
                id = 1,
                type = "general",
                setup = "Why did the chicken cross the road?",
                delivery = "To get to the other side",
                joke = null
            ),
            JokeOrSetUp(
                id = 2,
                type = "programming",
                setup = null,
                delivery = null,
                joke = "Why do programmers prefer dark mode? Because light attracts bugs!"
            )
        )

        fakeApiService.jokesResponse = mockedJokes

        val response: Response<JokesResponse> = fakeApiService.fetchAllJokes("general", 2)

        assertEquals(2, response.body()?.jokes?.size)
        assertEquals(mockedJokes, response.body()?.jokes)
    }
}