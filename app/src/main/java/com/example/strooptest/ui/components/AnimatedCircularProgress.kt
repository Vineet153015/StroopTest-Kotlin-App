package com.example.strooptest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strooptest.ui.theme.StroopColors
import kotlin.math.roundToInt

@Composable
fun AnimatedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f,
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
    progressColor: Color = StroopColors.AccentBlue
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutCubic
        )
    )

    val progressAngle = 360f * animatedProgress
    val percentage = (animatedProgress * 100).roundToInt()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = (minOf(canvasWidth, canvasHeight) / 2) - strokeWidth

            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = radius,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = progressAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Percentage text
        Text(
            text = "$percentage%",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}