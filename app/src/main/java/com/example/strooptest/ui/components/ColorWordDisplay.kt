package com.example.strooptest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strooptest.data.StroopWord

@Composable
fun ColorWordDisplay(
    stroopWord: StroopWord,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val pulseScale by animateFloatAsState(
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // ✅ CLEANED UP: Only the main word, no instruction texts
        Text(
            text = stroopWord.text,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = stroopWord.color,
            textAlign = TextAlign.Center,
            modifier = Modifier.scale(scale * pulseScale)
        )
    }
}