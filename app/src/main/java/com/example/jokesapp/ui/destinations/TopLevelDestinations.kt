package com.example.jokesapp.ui.destinations

sealed class TopLevelDestinations(val route: String) {
    data object Home : TopLevelDestinations("home_screen")
    data object BookMarks : TopLevelDestinations("bookmarks_screen")
    data object Delete : TopLevelDestinations("delete_screen")
}