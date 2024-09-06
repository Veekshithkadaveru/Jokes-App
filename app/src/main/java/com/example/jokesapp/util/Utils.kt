package com.example.jokesapp.util

import android.content.Context
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

fun addSoundEffect(view: View) {
    view.playSoundEffect(SoundEffectConstants.CLICK)
}
@Composable
fun CustomRowWith2Values(modifier: Modifier = Modifier, value1: String, value2: String) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = value1,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = value2)
    }
}
