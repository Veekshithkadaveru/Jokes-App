package com.example.jokesapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jokesapp.ui.destinations.TopLevelDestinations
import com.example.jokesapp.ui.screens.BookmarksScreen
import com.example.jokesapp.ui.screens.DeleteScreen
import com.example.jokesapp.ui.screens.JokesScreen
import com.example.jokesapp.ui.viewmodel.JokesViewmodel

@Composable
fun AppNavigation(viewModel: JokesViewmodel, navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = TopLevelDestinations.Home.route) {
        composable(route = TopLevelDestinations.Home.route) {
            JokesScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
        composable(route = TopLevelDestinations.BookMarks.route) {
            BookmarksScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
        composable(route = TopLevelDestinations.Delete.route) {
            DeleteScreen(viewmodel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }
}