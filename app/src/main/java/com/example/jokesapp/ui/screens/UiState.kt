package com.example.jokesapp.ui.screens

import com.example.jokesapp.data.local.JokesEntity

sealed class UIstate {
    data object Initial : UIstate()
    data object Loading : UIstate()
    class Success(val jokesList: List<JokesEntity>) : UIstate()
    class Error(val message: String) : UIstate()
}