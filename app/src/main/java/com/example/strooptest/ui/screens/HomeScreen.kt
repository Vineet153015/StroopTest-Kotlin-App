package com.example.strooptest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strooptest.R
import com.example.strooptest.ui.theme.StroopColors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onRequestPermission: () -> Unit,
    showCountdown: Boolean = false,
    countdownNumber: Int = 3
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        StroopColors.GameBackground,
                        Color(0xFF1E293B)
                    )
                )
            )
    ) {
        // ✅ Main content - this won't recompose during countdown
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Top Title
            Text(
                text = "Stroop Test",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.Start)
            )

            // Center Illustration
            Image(
                painter = painterResource(id = R.drawable.strooper),
                contentDescription = "Brain Illustration",
                modifier = Modifier
                    .height(200.dp)
                    .width(180.dp)
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Description Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🧪", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "What is the Stroop Test?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "A psychological task that measures attention and cognitive flexibility. Identify the color of words, not what they spell - even when they conflict!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        lineHeight = 25.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📝", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How to Take the Test",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val instructions = listOf(
                        "• See color words (Red, Blue, Green)",
                        "• Select the text color, not the word",
                        "• Example: \"Green\" in red = answer \"Red\"",
                        "• Stay focused and respond quickly"
                    )

                    instructions.forEach {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black.copy(alpha = 0.8f),
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartGame, // ✅ Simplified - only call onStartGame
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,   // Black background
                    contentColor = Color.White      // White text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Start Test", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        // ✅ Countdown Overlay - smooth animation without screen flicker
        AnimatedVisibility(
            visible = showCountdown,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                // ✅ Animate the countdown number change
                AnimatedContent(
                    targetState = countdownNumber,
                    transitionSpec = {
                        (scaleIn(animationSpec = tween(300)) +
                                fadeIn(animationSpec = tween(300))) with
                                (scaleOut(animationSpec = tween(150)) +
                                        fadeOut(animationSpec = tween(150)))
                    }
                ) { number ->
                    Text(
                        text = number.toString(),
                        fontSize = 140.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}