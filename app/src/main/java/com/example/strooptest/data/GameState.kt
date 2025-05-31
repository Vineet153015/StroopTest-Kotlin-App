//
//package com.example.strooptest.data
//
//import androidx.compose.ui.graphics.Color
//
//data class StroopWord(
//    val text: String,
//    val color: Color,
//    val colorName: String
//)
//
//data class GameResult(
//    val totalQuestions: Int,
//    val correctAnswers: Int,
//    val wrongAnswers: Int,
//    val score: Int
//) {
//    val percentage: Int
//        get() = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
//}
//
//sealed class GameState {
//    object Home : GameState()
//    object Countdown : GameState() // ✅ FIXED: No parameter, just indicates countdown screen
//
//    // ✅ FIXED: Removed timeLeft and isListening - they're now separate
//    data class Playing(
//        val currentWord: StroopWord,
//        val questionNumber: Int = 1
//    ) : GameState()
//
//    data class ScoreCalculating(val progress: Float) : GameState()
//    data class Results(val result: GameResult) : GameState()
//}
//
//enum class AudioState {
//    IDLE,
//    LISTENING,
//    PROCESSING
//}

package com.example.strooptest.data

import androidx.compose.ui.graphics.Color

data class StroopWord(
    val text: String,
    val color: Color,
    val colorName: String
)

data class GameResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val score: Int
) {
    val percentage: Int
        get() = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
}

sealed class GameState {
    object Home : GameState()
    object Countdown : GameState() // ✅ FIXED: No parameter, just indicates countdown screen

    // ✅ FIXED: Removed timeLeft and isListening - they're now separate
    data class Playing(
        val currentWord: StroopWord,
        val questionNumber: Int = 1
    ) : GameState()

    data class ScoreCalculating(val progress: Float) : GameState()
    data class Results(val result: GameResult) : GameState()
}

enum class AudioState {
    IDLE,
    LISTENING,
    PROCESSING
}

// ✅ NEW: Gaze detection state
data class GazeDetectionState(
    val isEnabled: Boolean = true,
    val sessionId: String = "",
    val currentQuestion: String = "",
    val currentAnswer: String = "",
    val lastCorrectResponse: Boolean = false
)