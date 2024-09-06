package com.example.jokesapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jokesapp.ui.viewmodel.JokesViewmodel
import com.example.jokesapp.util.DismissButton
import com.example.jokesapp.util.addSoundEffect

@Composable
fun DeleteScreen(
    modifier: Modifier,
   viewmodel: JokesViewmodel= hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(true) }
    val view = LocalView.current
    Column(modifier = modifier) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when clicked outside
                    showDialog = false
                },
                title = {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Delete Unbookmarked Jokes?",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                text = {
                    Text(
                        text = "This will delete all the unbookmarked jokes from the device.",
                    )
                },
                dismissButton = {
                    DismissButton {
                        addSoundEffect(view)
                        showDialog = false
                    }
                },
                confirmButton = {
                    OutlinedButton(
                        onClick = {
                            // Action when confirm button is clicked
                            addSoundEffect(view)
                            viewmodel.deleteUnbookmarkedJokes()
                            showDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            )
        }
    }
}


