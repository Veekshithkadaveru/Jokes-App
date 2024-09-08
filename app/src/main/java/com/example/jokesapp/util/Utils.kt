package com.example.jokesapp.util

import android.content.Context
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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

@Composable
fun DismissButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "Dismiss")
    }
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

@Composable
fun VerticalSpacer(height: Dp = 4.dp) {
    Spacer(modifier = Modifier.height(height = height))
}
@Composable
fun DotsCollision() {
    val dotSize = 24.dp
    val maxOffset = 30f
    val delayUnit = 500 // it's better to use longer delay for this animation
    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(x = offset.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    )
    val infiniteTransition = rememberInfiniteTransition()
    val offsetLeft by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 3
                0f at 0 using LinearEasing
                -maxOffset at delayUnit / 2 using LinearEasing
                0f at delayUnit
            }
        ), label = ""
    )
    val offsetRight by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 3
                0f at delayUnit using LinearEasing
                maxOffset at delayUnit + delayUnit / 2 using LinearEasing
                0f at delayUnit * 2
            }
        ), label = ""
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = maxOffset.dp)
    ) {
        val spaceSize = 2.dp
        Dot(offsetLeft)
        Spacer(Modifier.width(spaceSize))
        Dot(0f)
        Spacer(Modifier.width(spaceSize))
        Dot(offsetRight)
    }
}

