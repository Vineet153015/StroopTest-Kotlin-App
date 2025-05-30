
package com.example.strooptest.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.example.strooptest.data.GameState
import com.example.strooptest.ui.screens.*
import com.example.strooptest.viewmodel.StroopGameViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StroopNavigation(
    viewModel: StroopGameViewModel,
    onRequestPermission: () -> Unit
) {
    val gameState by viewModel.gameState
    val timeLeft by viewModel.timeLeft
    val countdownNumber by viewModel.countdownNumber
    val isListening by viewModel.isListening

    // ✅ KEY FIX: Create logical screen states that don't change during content updates
    val currentScreen = when (gameState) {
        is GameState.Home -> "home"
        is GameState.Countdown -> "home" // ✅ Stay on home screen during countdown
        is GameState.Playing -> "game"   // ✅ Stay on game screen between questions
        is GameState.ScoreCalculating -> "score"
        is GameState.Results -> "results"
    }

    AnimatedContent(
        targetState = currentScreen, // ✅ Only animate on actual screen changes
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) with
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300))
        }
    ) { screen ->
        when (screen) {
            "home" -> {
                // ✅ Pass all needed states to HomeScreen - it handles countdown internally
                HomeScreen(
                    onStartGame = {
                        onRequestPermission()
                        viewModel.startGame()
                    },
                    onRequestPermission = onRequestPermission,
                    showCountdown = gameState is GameState.Countdown,
                    countdownNumber = countdownNumber
                )
            }
            "game" -> {
                // ✅ Only render when we actually have a Playing state
                if (gameState is GameState.Playing) {
                    GameScreen(
                        stroopWord = (gameState as GameState.Playing).currentWord,
                        timeLeft = timeLeft,
                        isListening = isListening,
                        questionNumber = (gameState as GameState.Playing).questionNumber,
                        totalQuestions = 10,
                        onStartListening = { viewModel.startListening() },
                        onStopListening = { viewModel.stopListening() }
                    )
                }
            }
            "score" -> {
                if (gameState is GameState.ScoreCalculating) {
                    ScoreCalculationScreen(progress = (gameState as GameState.ScoreCalculating).progress)
                }
            }
            "results" -> {
                if (gameState is GameState.Results) {
                    ResultScreen(
                        result = (gameState as GameState.Results).result,
                        onBackToHome = { viewModel.goHome() }
                    )
                }
            }
        }
    }
}