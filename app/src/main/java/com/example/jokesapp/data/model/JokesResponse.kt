package com.example.jokesapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class JokesResponse(
    @Json(name = "jokes")
    val jokes: List<JokeOrSetUp>
)

@JsonClass(generateAdapter = true)
data class JokeOrSetUp(
    @Json(name = "id")
    val id: Int,
    @Json(name = "type")
    val type: String,
    @Json(name = "setup")
    val setup: String?,
    @Json(name = "delivery")
    val delivery: String?,
    @Json(name = "Joke")
    val joke: String?
)