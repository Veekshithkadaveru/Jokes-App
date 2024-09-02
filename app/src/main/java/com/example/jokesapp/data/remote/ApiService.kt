package com.example.jokesapp.data.remote

import com.example.jokesapp.data.model.JokesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("joke/{genre}")
    suspend fun fetchAllJokes(
        @Path("genre") genre:String,
        @Query("amount") amount:Int
    ):Response<JokesResponse>
}