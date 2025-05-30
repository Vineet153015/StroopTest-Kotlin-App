package com.example.strooptest.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strooptest.ui.theme.StroopColors

@Composable
fun CountdownScreen(count: Int) {
    val scale by animateFloatAsState(
        targetValue = 1.2f,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOutBounce
        )
    )

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StroopColors.GameBackground),
        contentAlignment = Alignment.Center
    ) {
        // Pulsing background circle
        Box(
            modifier = Modifier
                .size(300.dp)
                .scale(scale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            StroopColors.AccentBlue.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        )

        // Countdown number
        Text(
            text = count.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = alpha),
            style = MaterialTheme.typography.headlineLarge
        )

        // Small text below
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = 100.dp)
        ) {
            Text(
                text = "Get Ready",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Test starting in $count...",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}