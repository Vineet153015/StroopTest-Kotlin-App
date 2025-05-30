//package com.example.strooptest.viewmodel
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.State
//import androidx.compose.ui.graphics.Color
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.strooptest.data.AudioState
//import com.example.strooptest.data.GameState
//import com.example.strooptest.data.*
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class StroopGameViewModel : ViewModel() {
//
//    private val _gameState = mutableStateOf<GameState>(GameState.Home)
//    val gameState: State<GameState> = _gameState
//
//    private val _audioState = mutableStateOf(AudioState.IDLE)
//    val audioState: State<AudioState> = _audioState
//
//    private var hasAudioPermission = false
//    private var currentQuestionIndex = 0
//    private var correctAnswers = 0
//    private var wrongAnswers = 0
//    private val totalQuestions = 10
//
//    private val colorWords = listOf(
//        StroopWord("Red", Color.Blue, "blue"),
//        StroopWord("Blue", Color.Green, "green"),
//        StroopWord("Green", Color.Red, "red"),
//        StroopWord("Yellow", Color.Magenta, "magenta"),
//        StroopWord("Purple", Color.Yellow, "yellow"),
//        StroopWord("Orange", Color.Cyan, "cyan"),
//        StroopWord("Pink", Color(0xFF8B4513), "brown"),
//        StroopWord("Brown", Color(0xFFFFC0CB), "pink"),
//        StroopWord("Black", Color(0xFFFFFFFF), "white"),
//        StroopWord("White", Color.Black, "black")
//    )
//
//    fun setAudioPermission(granted: Boolean) {
//        hasAudioPermission = granted
//    }
//
//    fun startGame() {
//        if (!hasAudioPermission) return
//
//        resetGame()
////        startCountdown()
//    }
//
//    private fun resetGame() {
//        currentQuestionIndex = 0
//        correctAnswers = 0
//        wrongAnswers = 0
//    }
//
////    private fun startCountdown() {
////        viewModelScope.launch {
////            for (count in 3 downTo 1) {
////                _gameState.value = GameState.Countdown(count)
////                delay(1000)
////            }
////            startPlaying()
////        }
////    }
//
//    private fun startPlaying() {
//        if (currentQuestionIndex < totalQuestions) {
//            val currentWord = colorWords[currentQuestionIndex]
//            _gameState.value = GameState.Playing(
//                currentWord = currentWord,
//                timeLeft = 42,
//                questionNumber = currentQuestionIndex + 1
//            )
//            startTimer()
//        } else {
//            calculateScore()
//        }
//    }
//
//    private fun startTimer() {
//        viewModelScope.launch {
//            var timeLeft = 42
//            while (timeLeft > 0 && _gameState.value is GameState.Playing) {
//                delay(1000)
//                timeLeft--
//                val currentState = _gameState.value as? GameState.Playing
//                currentState?.let {
//                    _gameState.value = it.copy(timeLeft = timeLeft)
//                }
//            }
//
//            if (_gameState.value is GameState.Playing) {
//                // Time's up, move to next question
//                wrongAnswers++
//                nextQuestion()
//            }
//        }
//    }
//
//    fun startListening() {
//        _audioState.value = AudioState.LISTENING
//        val currentState = _gameState.value as? GameState.Playing
//        currentState?.let {
//            _gameState.value = it.copy(isListening = true)
//        }
//
//        // Simulate audio processing
//        viewModelScope.launch {
//            delay(2000) // Simulate listening time
//            _audioState.value = AudioState.PROCESSING
//            delay(1000) // Simulate processing time
//
//            // Simulate random correct/incorrect answer
//            val isCorrect = (0..1).random() == 1
//            processAnswer(isCorrect)
//        }
//    }
//
//    fun stopListening() {
//        _audioState.value = AudioState.IDLE
//        val currentState = _gameState.value as? GameState.Playing
//        currentState?.let {
//            _gameState.value = it.copy(isListening = false)
//        }
//    }
//
//    private fun processAnswer(isCorrect: Boolean) {
//        if (isCorrect) {
//            correctAnswers++
//        } else {
//            wrongAnswers++
//        }
//
//        _audioState.value = AudioState.IDLE
//        nextQuestion()
//    }
//
//    private fun nextQuestion() {
//        currentQuestionIndex++
//
//        if (currentQuestionIndex < totalQuestions) {
//            viewModelScope.launch {
//                delay(500) // Brief pause between questions
//                startPlaying()
//            }
//        } else {
//            calculateScore()
//        }
//    }
//
//    private fun calculateScore() {
//        viewModelScope.launch {
//            _gameState.value = GameState.ScoreCalculating(0f)
//
//            // Animate progress from 0 to 100%
//            for (progress in 0..100 step 2) {
//                _gameState.value = GameState.ScoreCalculating(progress / 100f)
//                delay(50)
//            }
//
//            delay(500) // Brief pause at 100%
//
//            val result = GameResult(
//                totalQuestions = totalQuestions,
//                correctAnswers = correctAnswers,
//                wrongAnswers = wrongAnswers,
//                score = correctAnswers
//            )
//
//            _gameState.value = GameState.Results(result)
//        }
//    }
//
//    fun goHome() {
//        _gameState.value = GameState.Home
//        _audioState.value = AudioState.IDLE
//    }
//}
package com.example.strooptest.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strooptest.data.AudioState
import com.example.strooptest.data.GameState
import com.example.strooptest.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StroopGameViewModel : ViewModel() {

    private val _gameState = mutableStateOf<GameState>(GameState.Home)
    val gameState: State<GameState> = _gameState

    private val _audioState = mutableStateOf(AudioState.IDLE)
    val audioState: State<AudioState> = _audioState

    // ✅ NEW: Separate states to avoid navigation flickering
    private val _timeLeft = mutableStateOf(42)
    val timeLeft: State<Int> = _timeLeft

    private val _countdownNumber = mutableStateOf(3)
    val countdownNumber: State<Int> = _countdownNumber

    private val _isListening = mutableStateOf(false)
    val isListening: State<Boolean> = _isListening

    private var hasAudioPermission = false
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var wrongAnswers = 0
    private val totalQuestions = 10

    private val colorWords = listOf(
        StroopWord("Red", Color.Blue, "blue"),
        StroopWord("Blue", Color.Green, "green"),
        StroopWord("Green", Color.Red, "red"),
        StroopWord("Yellow", Color.Magenta, "magenta"),
        StroopWord("Purple", Color.Yellow, "yellow"),
        StroopWord("Orange", Color.Cyan, "cyan"),
        StroopWord("Pink", Color(0xFF8B4513), "brown"),
        StroopWord("Brown", Color(0xFFFFC0CB), "pink"),
        StroopWord("Black", Color(0xFFFFFFFF), "white"),
        StroopWord("White", Color.Black, "black")
    )

    fun setAudioPermission(granted: Boolean) {
        hasAudioPermission = granted
    }

    fun startGame() {
        resetGame()
        startCountdown()
    }

    private fun resetGame() {
        currentQuestionIndex = 0
        correctAnswers = 0
        wrongAnswers = 0
        _isListening.value = false
    }

    // ✅ FIXED: Only change to Countdown state once, then update countdown number separately
    private fun startCountdown() {
        _gameState.value = GameState.Countdown // ← Set once, no parameter
        viewModelScope.launch {
            for (count in 3 downTo 1) {
                _countdownNumber.value = count // ← Update separately, no navigation trigger
                delay(1000)
            }
            startPlaying()
        }
    }

//    private fun startPlaying() {
//        if (currentQuestionIndex < totalQuestions) {
//            val currentWord = colorWords[currentQuestionIndex]
//            // ✅ FIXED: Set game state once per question
//            _gameState.value = GameState.Playing(
//                currentWord = currentWord,
//                questionNumber = currentQuestionIndex + 1
//            )
//            _timeLeft.value = 42
//            _isListening.value = false
//            startTimer()
//        } else {
//            calculateScore()
//        }
//    }
private fun startPlaying() {
    if (currentQuestionIndex < totalQuestions) {
        val currentWord = colorWords[currentQuestionIndex]
        _gameState.value = GameState.Playing(
            currentWord = currentWord,
            questionNumber = currentQuestionIndex + 1
        )
        _isListening.value = false
        // Timer continues from previous value, no reset
        if (currentQuestionIndex == 0) {
            _timeLeft.value = 42  // Only set initial time for first question
            startTimer()  // Only start timer once
        }
    } else {
        calculateScore()
    }
}

    // ✅ FIXED: Timer only updates _timeLeft, not _gameState
    private fun startTimer() {
        viewModelScope.launch {
            var timeLeft = _timeLeft.value

            while (timeLeft > 0 && _gameState.value is GameState.Playing) {
                delay(1000)
                timeLeft--
                _timeLeft.value = timeLeft
            }

            // ✅ When timer ends, finish the entire game
            if (_gameState.value is GameState.Playing) {
                // Mark remaining questions as wrong
                wrongAnswers += (totalQuestions - currentQuestionIndex)
                calculateScore()  // Go directly to score calculation
            }
        }
    }

    // ✅ FIXED: Only update listening state, not game state
    fun startListening() {
        _audioState.value = AudioState.LISTENING
        _isListening.value = true // ← Update separate state, no navigation trigger

        viewModelScope.launch {
            delay(2000)
            _audioState.value = AudioState.PROCESSING
            delay(1000)

            val isCorrect = (0..1).random() == 1
            processAnswer(isCorrect)
        }
    }

    // ✅ FIXED: Only update listening state, not game state
    fun stopListening() {
        _audioState.value = AudioState.IDLE
        _isListening.value = false // ← Update separate state, no navigation trigger
    }

    private fun processAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            correctAnswers++
        } else {
            wrongAnswers++
        }

        _audioState.value = AudioState.IDLE
        _isListening.value = false
        nextQuestion()
    }

    private fun nextQuestion() {
        currentQuestionIndex++

        if (currentQuestionIndex < totalQuestions) {
            viewModelScope.launch {
                delay(500)
                startPlaying()
            }
        } else {
            calculateScore()
        }
    }

    private fun calculateScore() {
        viewModelScope.launch {
            _gameState.value = GameState.ScoreCalculating(0f)

            for (progress in 0..100 step 2) {
                _gameState.value = GameState.ScoreCalculating(progress / 100f)
                delay(50)
            }

            delay(500)

            val result = GameResult(
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                wrongAnswers = wrongAnswers,
                score = correctAnswers
            )

            _gameState.value = GameState.Results(result)
        }
    }

    fun goHome() {
        _gameState.value = GameState.Home
        _audioState.value = AudioState.IDLE
        _isListening.value = false
    }
}