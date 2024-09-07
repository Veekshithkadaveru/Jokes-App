package com.example.jokesapp.ui.screens

import android.content.Intent
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jokesapp.ui.viewmodel.JokesViewmodel
import com.example.jokesapp.util.DismissButton
import com.example.jokesapp.util.ErrorMessage
import com.example.jokesapp.util.LoadIndicator
import com.example.jokesapp.util.VerticalSpacer
import com.example.jokesapp.util.addSoundEffect
import com.example.jokesapp.util.toastMsg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(modifier: Modifier, viewModel: JokesViewmodel = hiltViewModel()) {

    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var jokeToShare by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchBookMarkedJokes()
    }
    val bookmarkUIState by viewModel.bookmarkUIstate.collectAsState()
    when (bookmarkUIState) {
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
                val error = (bookmarkUIState as UIstate.Error).message
                ErrorMessage(error = error)
            }
        }

        is UIstate.Success -> {
            val successState = bookmarkUIState as UIstate.Success
            if (successState.jokesList.isEmpty()) {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorMessage(error = "You don't have any Bookmarks")
                }
            } else {
                LazyColumn(modifier = modifier) {
                    items(successState.jokesList, key = { joke ->
                        joke.id
                    }) { joke ->
                        val context = LocalContext.current
                        val dismissState = rememberSwipeToDismissBoxState()
                        LaunchedEffect(key1 = dismissState.currentValue) {
                            when (dismissState.currentValue) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    toastMsg(context = context, msg = "Message Deleted")
                                    viewModel.deleteJokeViaId(joke.id)
                                }

                                else -> {}
                            }
                        }
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val backgroundColor by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.EndToStart ->
                                            Color.Red.copy(alpha = 0.8f)

                                        else -> Color.White
                                    }, label = ""
                                )
                                val iconScale by animateFloatAsState(
                                    targetValue = if (
                                        dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
                                    ) 1.3f else 0.5f,
                                    label = ""
                                )
                                Box(
                                    Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color = backgroundColor)
                                        .padding(16.dp),
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .scale(iconScale)
                                            .align(Alignment.CenterEnd),
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            })
                        {
                            //Add Joke Item here
                            JokeItem(unbookmarkedJoke = joke, jokePressed = { joke ->
                                jokeToShare = if (joke.type == "single") {
                                    "Joke:${joke.jokeMessage}"
                                } else {
                                    "Setup:${joke.setup}\n Punchline:${joke.punchLine}"
                                }
                                showBottomSheet = true
                            }) { isBookmarked ->
                                toastMsg(context = context, msg = "Joke Unbookmarked")
                                viewModel.updateBookmarkStatus(joke.id, isBookmarked)
                            }
                        }
                    }
                }
            }
        }

        else -> {}
    }
    val shareLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {}
    val view: View = LocalView.current
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Share it with your Loved ones!",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer()
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DismissButton {
                        addSoundEffect(view)
                        showBottomSheet = false
                    }
                    OutlinedButton(onClick = {
                        addSoundEffect(view)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, jokeToShare)
                        }
                        val chooser = Intent.createChooser(intent, "Share joke via...")
                        shareLauncher.launch(chooser)
                    }) {
                        Text(text = "Share")
                    }
                }
            }
        }
    }
}