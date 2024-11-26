package com.example.jokesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes_entity")
data class JokesEntity(
    @PrimaryKey
    val id: Int,
    val type: String,
    val setup: String?,
    val punchLine: String?,
    val jokeMessage: String?,
    var isBookMarked: Boolean = false
)