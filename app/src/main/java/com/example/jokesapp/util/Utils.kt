package com.example.jokesapp.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadIndicator() {
    LinearProgressIndicator()
}

@Composable
fun ErrorMessage(error: String) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = error,
        color = MaterialTheme.colorScheme.error
    )
}


fun toastMsg(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
