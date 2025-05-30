//package com.example.strooptest.ui.screens
//
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.strooptest.data.GameResult
//import com.example.strooptest.ui.components.GradientButton
//import com.example.strooptest.ui.components.ScoreCard
//import com.example.strooptest.ui.theme.StroopColors
//
//@Composable
//fun ResultScreen(
//    result: GameResult,
//    onBackToHome: () -> Unit
//) {
//    val celebrationScale by animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioLowBouncy,
//            stiffness = Spring.StiffnessLow
//        )
//    )
//
//    val particleAnimation by animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        StroopColors.GameBackground,
//                        Color(0xFF1A1A2E)
//                    )
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // Celebration emoji with animation
//            Box(
//                modifier = Modifier
//                    .scale(celebrationScale)
//                    .size(80.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "🎉",
//                    fontSize = 64.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Title with gradient text effect
//            Text(
//                text = "Stroop Test Complete!",
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // Score display
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = StroopColors.CardBackground
//                )
//            ) {
//                Column(
//                    modifier = Modifier.padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Your Score",
//                        color = Color.White.copy(alpha = 0.7f),
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = result.score.toString(),
//                        fontSize = 48.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    val encouragementText = when {
//                        result.percentage >= 80 -> "Excellent Focus!"
//                        result.percentage >= 60 -> "Good Job!"
//                        result.percentage >= 40 -> "Keep Practicing!"
//                        else -> "Keep Trying!"
//                    }
//
//                    val encouragementColor = when {
//                        result.percentage >= 80 -> StroopColors.AccentGreen
//                        result.percentage >= 60 -> StroopColors.AccentBlue
//                        result.percentage >= 40 -> StroopColors.AccentYellow
//                        else -> StroopColors.AccentRed
//                    }
//
//                    Text(
//                        text = encouragementText,
//                        color = encouragementColor,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Text(
//                        text = "Keep practicing to improve your focus.",
//                        color = Color.White.copy(alpha = 0.6f),
//                        fontSize = 14.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Test Report Section
//            Text(
//                text = "📊 Test Report",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Score breakdown
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                ScoreCard(
//                    icon = "❓",
//                    label = "Total Questions",
//                    value = result.totalQuestions.toString(),
//                    color = StroopColors.AccentBlue,
//                    modifier = Modifier.weight(1f)
//                )
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                ScoreCard(
//                    icon = "✅",
//                    label = "Correct",
//                    value = result.correctAnswers.toString(),
//                    color = StroopColors.AccentGreen,
//                    modifier = Modifier.weight(1f)
//                )
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                ScoreCard(
//                    icon = "❌",
//                    label = "Wrong",
//                    value = result.wrongAnswers.toString(),
//                    color = StroopColors.AccentRed,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Back to Home Button
//            GradientButton(
//                text = "🏠 Back to Home",
//                onClick = onBackToHome,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
package com.example.strooptest.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin
import kotlin.math.PI
import com.example.strooptest.data.GameResult
import com.example.strooptest.ui.components.GradientButton
import com.example.strooptest.ui.components.ScoreCard
import com.example.strooptest.ui.theme.StroopColors

@Composable
fun ResultScreen(
    result: GameResult,
    onBackToHome: () -> Unit
) {
    // Main celebration animation
    val celebrationScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Continuous pulsing animation for the icon
    val infiniteTransition = rememberInfiniteTransition()

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Glow animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Rotation animation for particles
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
    ) {
        // Animated particles background
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height

            // Draw floating particles
            for (i in 0..20) {
                val x = (width * 0.1f + (width * 0.8f * i / 20f) +
                        30f * sin((rotation + i * 18f) * PI / 180f).toFloat())
                val y = (height * 0.15f + (height * 0.7f * (i % 7) / 7f) +
                        20f * sin((rotation + i * 25f) * PI / 180f).toFloat())

                drawCircle(
                    color = when (i % 4) {
                        0 -> Color(0xFF4A90E2).copy(alpha = 0.6f)
                        1 -> Color(0xFF7B68EE).copy(alpha = 0.5f)
                        2 -> Color(0xFFFF6B6B).copy(alpha = 0.4f)
                        else -> Color(0xFFFFD93D).copy(alpha = 0.5f)
                    },
                    radius = (3f + (i % 3) * 2f),
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Celebration icon with glow and pulse effect
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect layers
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(pulseScale * 1.3f)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4A90E2).copy(alpha = glowAlpha * 0.6f),
                                    Color.Transparent
                                ),
                                radius = 80f
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .blur(20.dp)
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(pulseScale * 1.1f)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF7B68EE).copy(alpha = glowAlpha * 0.8f),
                                    Color.Transparent
                                ),
                                radius = 60f
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .blur(15.dp)
                )

                // Main celebration icon
                Box(
                    modifier = Modifier
                        .scale(celebrationScale * pulseScale)
                        .size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 64.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title with gradient text effect
            Text(
                text = "Stroop Test Complete!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4A90E2), // Blue
                            Color(0xFF50C878)  // Green
                        )
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // "Your Score" text above the card
            Text(
                text = "Your Score",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score display card with only the number
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3E4A6B).copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.score.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Encouragement text below the card
            val (encouragementText, encouragementColor) = when {
                result.percentage >= 80 -> "Excellent Focus!" to Color(0xFF4CAF50)
                result.percentage >= 60 -> "Good Job!" to Color(0xFF2196F3)
                result.percentage >= 40 -> "Keep Practicing!" to Color(0xFFFF9800)
                else -> "Keep Trying!" to Color(0xFFFF5722)
            }

            Text(
                text = encouragementText,
                color = encouragementColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Keep practicing to improve your focus.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Test Report Section with icon - centered
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "📊",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Test Report",
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4A90E2), // Blue
                                Color(0xFF50C878)  // Green
                            )
                        )
                    ),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Enhanced score breakdown cards with glow effects
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GlowingScoreCard(
                    icon = "❓",
                    label = "Total Questions",
                    value = result.totalQuestions.toString(),
                    cardColor = Color(0xFF1E3A5F),
                    glowColor = Color(0xFF4A90E2),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                GlowingScoreCard(
                    icon = "✅",
                    label = "Correct",
                    value = result.correctAnswers.toString(),
                    cardColor = Color(0xFF1E4A2F),
                    glowColor = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                GlowingScoreCard(
                    icon = "❌",
                    label = "Wrong",
                    value = result.wrongAnswers.toString(),
                    cardColor = Color(0xFF4A1E1E),
                    glowColor = Color(0xFFFF5722),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Enhanced Back to Home Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF667eea),
                                    Color(0xFF764ba2)
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = onBackToHome,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "🏠",
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Back to Home",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GlowingScoreCard(
    icon: String,
    label: String,
    value: String,
    cardColor: Color,
    glowColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Glow effect background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            glowColor.copy(alpha = glowAlpha * 0.3f),
                            Color.Transparent
                        ),
                        radius = 100f
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .blur(8.dp)
        )

        // Main card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon with colored background circle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            glowColor.copy(alpha = 0.8f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Label text
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Value
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}