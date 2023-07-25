package com.example.owlio.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.owlio.ui.theme.OwlioAppTheme

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleColor: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.primary
    ),
    animationDuration: Int = 1000, // in ms
) {
    val rotateAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        rotateAnimatable.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }


    // Loading Display
    Box(
        modifier = modifier
            .size(64.dp)
            .aspectRatio(1f)
            .rotate(degrees = rotateAnimatable.value)
            .border(
                width = 6.dp,
                brush = Brush.sweepGradient(circleColor),
                shape = CircleShape
            )
    )
}


@Preview(showBackground = true)
@Composable
fun LoadingAnimationPreview() {
    OwlioAppTheme(useDarkTheme = false) {
        LoadingAnimation()
    }
}