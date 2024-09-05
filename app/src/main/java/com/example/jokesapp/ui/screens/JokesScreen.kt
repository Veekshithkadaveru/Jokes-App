package com.example.jokesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jokesapp.data.local.JokesEntity
import com.example.jokesapp.ui.viewmodel.JokesViewmodel
import com.example.jokesapp.util.ErrorMessage
import com.example.jokesapp.util.LoadIndicator
import com.example.jokesapp.util.toastMsg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokesScreen(
    modifier: Modifier,
    viewModel: JokesViewmodel = hiltViewModel()
) {
    val homeUIstate by viewModel.homeUIstate.collectAsState()
    when (homeUIstate) {
        is UIstate.Loading -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadIndicator()
            }
        }

        is UIstate.Error -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val error = (homeUIstate as UIstate.Error).message
                ErrorMessage(error = error)
            }
        }

        is UIstate.Success -> {
            val successState = homeUIstate as UIstate.Success
            if (successState.jokesList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorMessage(
                        error = "It seems no joke is stored locally! Check your bookmarked jokes." +
                                "\nIf nothing works you need to restart the app."
                    )
                }
            } else {
                LazyColumn(modifier = Modifier) {
                    items(successState.jokesList, key = { joke ->
                        joke.id
                    }) { joke ->
                        val context = LocalContext.current
                        val dismissState = rememberSwipeToDismissBoxState()
                        LaunchedEffect(key1 = dismissState.currentValue) {
                            when (dismissState.currentValue) {

                                SwipeToDismissBoxValue.EndToStart -> {
                                    toastMsg(context = context, msg = "Joke Deleted")
                                    viewModel.deleteJokeViaId(joke.id)
                                }

                                SwipeToDismissBoxValue.StartToEnd -> {
                                    toastMsg(context = context, msg = "Joke Book Marked")
                                    viewModel.updateBookmarkStatus(joke.id, bookMarked = true)
                                }

                                else -> {}
                            }
                        }
                        SwipeToDismissBox(state = dismissState, backgroundContent = {
                            SwipeToDismissBackgroundContent(dismissState)
                        }) {
                            JokeItem(unbookmarkedJoke = joke)
                            TODO()
                        }
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
fun JokeItem(unbookmarkedJoke: JokesEntity) {
    TODO()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissBackgroundContent(dismissState: SwipeToDismissBoxState) {
    TODO()
}
