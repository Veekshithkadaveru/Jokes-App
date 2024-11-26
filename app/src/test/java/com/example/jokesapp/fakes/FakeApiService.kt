package com.example.jokesapp.fakes

import com.example.jokesapp.data.model.JokeOrSetUp
import com.example.jokesapp.data.model.JokesResponse
import com.example.jokesapp.data.remote.ApiService
import retrofit2.Response

class FakeApiService:ApiService {
    var jokesResponse: List<JokeOrSetUp> = emptyList()
    override suspend fun fetchAllJokes(genre: String, amount: Int): Response<JokesResponse> {
        return Response.success(
            JokesResponse(jokes = jokesResponse)
        )
    }
}