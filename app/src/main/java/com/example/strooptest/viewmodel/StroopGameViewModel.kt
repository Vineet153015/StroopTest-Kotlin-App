//
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
//    // ✅ NEW: Separate states to avoid navigation flickering
//    private val _timeLeft = mutableStateOf(42)
//    val timeLeft: State<Int> = _timeLeft
//
//    private val _countdownNumber = mutableStateOf(3)
//    val countdownNumber: State<Int> = _countdownNumber
//
//    private val _isListening = mutableStateOf(false)
//    val isListening: State<Boolean> = _isListening
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
//        resetGame()
//        startCountdown()
//    }
//
//    private fun resetGame() {
//        currentQuestionIndex = 0
//        correctAnswers = 0
//        wrongAnswers = 0
//        _isListening.value = false
//    }
//
//    // ✅ FIXED: Only change to Countdown state once, then update countdown number separately
//    private fun startCountdown() {
//        _gameState.value = GameState.Countdown // ← Set once, no parameter
//        viewModelScope.launch {
//            for (count in 3 downTo 1) {
//                _countdownNumber.value = count // ← Update separately, no navigation trigger
//                delay(1000)
//            }
//            startPlaying()
//        }
//    }
//
//private fun startPlaying() {
//    if (currentQuestionIndex < totalQuestions) {
//        val currentWord = colorWords[currentQuestionIndex]
//        _gameState.value = GameState.Playing(
//            currentWord = currentWord,
//            questionNumber = currentQuestionIndex + 1
//        )
//        _isListening.value = false
//        // Timer continues from previous value, no reset
//        if (currentQuestionIndex == 0) {
//            _timeLeft.value = 42  // Only set initial time for first question
//            startTimer()  // Only start timer once
//        }
//    } else {
//        calculateScore()
//    }
//}
//
//    // ✅ FIXED: Timer only updates _timeLeft, not _gameState
//    private fun startTimer() {
//        viewModelScope.launch {
//            var timeLeft = _timeLeft.value
//
//            while (timeLeft > 0 && _gameState.value is GameState.Playing) {
//                delay(1000)
//                timeLeft--
//                _timeLeft.value = timeLeft
//            }
//
//            // ✅ When timer ends, finish the entire game
//            if (_gameState.value is GameState.Playing) {
//                // Mark remaining questions as wrong
//                wrongAnswers += (totalQuestions - currentQuestionIndex)
//                calculateScore()  // Go directly to score calculation
//            }
//        }
//    }
//
//    // ✅ FIXED: Only update listening state, not game state
//    fun startListening() {
//        _audioState.value = AudioState.LISTENING
//        _isListening.value = true // ← Update separate state, no navigation trigger
//
//        viewModelScope.launch {
//            delay(2000)
//            _audioState.value = AudioState.PROCESSING
//            delay(1000)
//
//            val isCorrect = (0..1).random() == 1
//            processAnswer(isCorrect)
//        }
//    }
//
//    // ✅ FIXED: Only update listening state, not game state
//    fun stopListening() {
//        _audioState.value = AudioState.IDLE
//        _isListening.value = false // ← Update separate state, no navigation trigger
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
//        _isListening.value = false
//        nextQuestion()
//    }
//
//    private fun nextQuestion() {
//        currentQuestionIndex++
//
//        if (currentQuestionIndex < totalQuestions) {
//            viewModelScope.launch {
//                delay(500)
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
//            for (progress in 0..100 step 2) {
//                _gameState.value = GameState.ScoreCalculating(progress / 100f)
//                delay(50)
//            }
//
//            delay(500)
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
//        _isListening.value = false
//    }
//}

package com.example.strooptest.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strooptest.data.AudioState
import com.example.strooptest.data.GameState
import com.example.strooptest.data.*
import com.example.strooptest.data.GazeDetectionDatabase
import com.example.strooptest.data.GazeDetectionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class StroopGameViewModel(application: Application) : AndroidViewModel(application) {

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

    // ✅ NEW: Gaze detection state
    private val _gazeDetectionState = mutableStateOf(GazeDetectionState())
    val gazeDetectionState: State<GazeDetectionState> = _gazeDetectionState

    // ✅ NEW: Database for storing gaze data
    private val gazeDatabase = GazeDetectionDatabase.getDatabase(application)
    private val gazeDao = gazeDatabase.gazeDao()

    private var hasAudioPermission = false
    private var hasCameraPermission = false
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

    // ✅ NEW: Set camera permission for gaze detection
    fun setCameraPermission(granted: Boolean) {
        hasCameraPermission = granted
        _gazeDetectionState.value = _gazeDetectionState.value.copy(
            isEnabled = granted
        )
    }

    fun startGame() {
        resetGame()
        // ✅ NEW: Initialize gaze detection session
        initializeGazeDetection()
        startCountdown()
    }

    private fun resetGame() {
        currentQuestionIndex = 0
        correctAnswers = 0
        wrongAnswers = 0
        _isListening.value = false
    }

    // ✅ NEW: Initialize gaze detection for this game session
    private fun initializeGazeDetection() {
        val sessionId = UUID.randomUUID().toString()
        _gazeDetectionState.value = GazeDetectionState(
            isEnabled = hasCameraPermission,
            sessionId = sessionId,
            currentQuestion = "",
            currentAnswer = "",
            lastCorrectResponse = false
        )
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

    private fun startPlaying() {
        if (currentQuestionIndex < totalQuestions) {
            val currentWord = colorWords[currentQuestionIndex]
            _gameState.value = GameState.Playing(
                currentWord = currentWord,
                questionNumber = currentQuestionIndex + 1
            )
            _isListening.value = false

            // ✅ NEW: Update gaze detection context
            updateGazeDetectionContext(currentWord)

            // Timer continues from previous value, no reset
            if (currentQuestionIndex == 0) {
                _timeLeft.value = 42  // Only set initial time for first question
                startTimer()  // Only start timer once
            }
        } else {
            calculateScore()
        }
    }

    // ✅ NEW: Update gaze detection with current game context
    private fun updateGazeDetectionContext(currentWord: StroopWord) {
        _gazeDetectionState.value = _gazeDetectionState.value.copy(
            currentQuestion = currentWord.text,
            currentAnswer = currentWord.colorName
        )
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

        // ✅ NEW: Update gaze detection with answer result
        _gazeDetectionState.value = _gazeDetectionState.value.copy(
            lastCorrectResponse = isCorrect
        )

        _audioState.value = AudioState.IDLE
        _isListening.value = false
        nextQuestion()
    }

    // ✅ NEW: Handle gaze detection data
    fun onGazeDetected(gazeData: GazeDetectionData) {
        viewModelScope.launch {
            try {
                gazeDao.insertGazeData(gazeData)
            } catch (e: Exception) {
                // Log error but don't interrupt game
                e.printStackTrace()
            }
        }
    }

    // ✅ NEW: Get current game context for gaze detector
    fun getCurrentGameContext(): Triple<String, String, Boolean> {
        val state = _gazeDetectionState.value
        return Triple(
            state.currentQuestion,
            state.currentAnswer,
            state.lastCorrectResponse
        )
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

    // ✅ NEW: Get all gaze data for analysis (optional)
    suspend fun getAllGazeData(): List<GazeDetectionData> {
        return try {
            gazeDao.getAllGazeData()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ✅ NEW: Clear gaze data (optional)
    suspend fun clearGazeData() {
        try {
            gazeDao.clearAllData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
