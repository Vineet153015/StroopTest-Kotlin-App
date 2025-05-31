//
//package com.example.strooptest.ui.screens
//
//import androidx.compose.animation.*
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.strooptest.data.StroopWord
//import com.example.strooptest.ui.components.ColorWordDisplay
//import com.example.strooptest.ui.components.MicrophoneButton
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun GameScreen(
//    stroopWord: StroopWord,
//    timeLeft: Int,
//    isListening: Boolean,
//    questionNumber: Int,
//    totalQuestions: Int,
//    onStartListening: () -> Unit,
//    onStopListening: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Timer at top left
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp),
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // ✅ Smooth timer updates - moved to top left
//                AnimatedContent(
//                    targetState = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
//                    transitionSpec = {
//                        if (targetState != initialState) {
//                            fadeIn(animationSpec = tween(200)) with
//                                    fadeOut(animationSpec = tween(200))
//                        } else {
//                            EnterTransition.None with ExitTransition.None
//                        }
//                    }
//                ) { timerText ->
//                    Text(
//                        text = timerText,
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(60.dp))
//
//            // ✅ Smooth word transitions - key fix for question changes
//            AnimatedContent(
//                targetState = stroopWord,
//                transitionSpec = {
//                    // Subtle scale and fade for word changes
//                    (scaleIn(
//                        initialScale = 0.8f,
//                        animationSpec = tween(400)
//                    ) + fadeIn(animationSpec = tween(400))) with
//                            (scaleOut(
//                                targetScale = 1.2f,
//                                animationSpec = tween(200)
//                            ) + fadeOut(animationSpec = tween(200)))
//                },
//                modifier = Modifier.weight(1f)
//            ) { word ->
//                ColorWordDisplay(
//                    stroopWord = word,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//
//            Spacer(modifier = Modifier.height(60.dp))
//
//            // ✅ Smooth instruction transitions - only show listening state
//            AnimatedContent(
//                targetState = isListening,
//                transitionSpec = {
//                    fadeIn(animationSpec = tween(300)) with
//                            fadeOut(animationSpec = tween(200))
//                }
//            ) { listening ->
//                if (listening) {
//                    Text(
//                        text = "Listening... Say the color!",
//                        color = Color.White,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.Center
//                    )
//                } else {
//                    // Empty space when not listening
//                    Spacer(modifier = Modifier.height(24.dp))
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Microphone button
//            MicrophoneButton(
//                isListening = isListening,
//                onStartListening = onStartListening,
//                onStopListening = onStopListening,
//                modifier = Modifier.size(120.dp)
//            )
//
//            Spacer(modifier = Modifier.height(40.dp))
//        }
//    }
//}

package com.example.strooptest.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strooptest.data.StroopWord
import com.example.strooptest.data.GazeDetectionData
import com.example.strooptest.ui.components.ColorWordDisplay
import com.example.strooptest.ui.components.MicrophoneButton
import com.example.strooptest.ui.components.CameraPreview

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameScreen(
    stroopWord: StroopWord,
    timeLeft: Int,
    isListening: Boolean,
    questionNumber: Int,
    totalQuestions: Int,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    // ✅ NEW: Gaze detection parameters
    gameSessionId: String = "",
    onGazeDetected: (GazeDetectionData) -> Unit = {},
    getCurrentGameContext: () -> Triple<String, String, Boolean> = { Triple("", "", false) },
    isGazeDetectionEnabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // ✅ NEW: Camera preview for gaze detection (top-right corner)
        if (isGazeDetectionEnabled && gameSessionId.isNotEmpty()) {
            CameraPreview(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                onGazeDetected = onGazeDetected,
                gameSessionId = gameSessionId,
                getCurrentGameContext = getCurrentGameContext,
                isVisible = true
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Timer at top left
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✅ Smooth timer updates - moved to top left
                AnimatedContent(
                    targetState = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                    transitionSpec = {
                        if (targetState != initialState) {
                            fadeIn(animationSpec = tween(200)) with
                                    fadeOut(animationSpec = tween(200))
                        } else {
                            EnterTransition.None with ExitTransition.None
                        }
                    }
                ) { timerText ->
                    Text(
                        text = timerText,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // ✅ Smooth word transitions - key fix for question changes
            AnimatedContent(
                targetState = stroopWord,
                transitionSpec = {
                    // Subtle scale and fade for word changes
                    (scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400))) with
                            (scaleOut(
                                targetScale = 1.2f,
                                animationSpec = tween(200)
                            ) + fadeOut(animationSpec = tween(200)))
                },
                modifier = Modifier.weight(1f)
            ) { word ->
                ColorWordDisplay(
                    stroopWord = word,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // ✅ Smooth instruction transitions - only show listening state
            AnimatedContent(
                targetState = isListening,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with
                            fadeOut(animationSpec = tween(200))
                }
            ) { listening ->
                if (listening) {
                    Text(
                        text = "Listening... Say the color!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Empty space when not listening
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Microphone button
            MicrophoneButton(
                isListening = isListening,
                onStartListening = onStartListening,
                onStopListening = onStopListening,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}