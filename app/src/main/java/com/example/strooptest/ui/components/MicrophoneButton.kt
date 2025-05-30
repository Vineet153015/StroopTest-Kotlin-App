package com.example.strooptest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.strooptest.ui.theme.StroopColors

@Composable
fun MicrophoneButton(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (isListening) 0.8f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    var isPressed by remember { mutableStateOf(false) }

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(100)
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer glow ring
        if (isListening) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(pulseScale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                StroopColors.AccentBlue.copy(alpha = glowAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Outer ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(pressScale)
                .border(
                    width = 3.dp,
                    color = if (isListening) StroopColors.AccentBlue else Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )

        // Inner button
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(pressScale)
                .clip(CircleShape)
                .background(
                    brush = if (isListening) {
                        Brush.radialGradient(
                            colors = listOf(
                                StroopColors.AccentBlue,
                                StroopColors.AccentBlue.copy(alpha = 0.8f)
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }
                )
                .clickable {
                    isPressed = true
                    if (isListening) {
                        onStopListening()
                    } else {
                        onStartListening()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Microphone icon (using text emoji as placeholder)
            androidx.compose.material3.Text(
                text = "🎤",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isListening) Color.White else Color.White.copy(alpha = 0.8f)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}