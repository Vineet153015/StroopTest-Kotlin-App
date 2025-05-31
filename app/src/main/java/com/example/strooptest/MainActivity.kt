//package com.example.strooptest
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.strooptest.ui.navigation.StroopNavigation
//import com.example.strooptest.ui.theme.StroopTestTheme
//import com.example.strooptest.viewmodel.StroopGameViewModel
//
//class MainActivity : ComponentActivity() {
//
//    private var hasAudioPermission by mutableStateOf(false)
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        hasAudioPermission = isGranted
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        checkAudioPermission()
//
//        setContent {
//            StroopTestTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val viewModel: StroopGameViewModel = viewModel()
//
//                    LaunchedEffect(hasAudioPermission) {
//                        viewModel.setAudioPermission(hasAudioPermission)
//                    }
//
//                    StroopNavigation(
//                        viewModel = viewModel,
//                        onRequestPermission = { requestAudioPermission() }
//                    )
//                }
//            }
//        }
//    }
//
//    private fun checkAudioPermission() {
//        hasAudioPermission = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.RECORD_AUDIO
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestAudioPermission() {
//        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//    }
//}

package com.example.strooptest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.strooptest.ui.navigation.StroopNavigation
import com.example.strooptest.ui.theme.StroopTestTheme
import com.example.strooptest.viewmodel.StroopGameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StroopGameViewModel by viewModels()

    // ✅ Audio permission launcher
    private val audioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setAudioPermission(isGranted)
        if (!isGranted) {
            Toast.makeText(this, "Audio permission is required to play the game", Toast.LENGTH_LONG).show()
        }
    }

    // ✅ NEW: Camera permission launcher for gaze detection
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setCameraPermission(isGranted)
        if (!isGranted) {
            Toast.makeText(this, "Camera permission required for gaze detection", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check initial permissions
        checkInitialPermissions()

        setContent {
            StroopTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StroopNavigation(
                        viewModel = viewModel,
                        onRequestPermission = { requestAudioPermission() },
                        // ✅ NEW: Camera permission callback
                        onRequestCameraPermission = { requestCameraPermission() }
                    )
                }
            }
        }
    }

    private fun checkInitialPermissions() {
        // Check audio permission
        val audioPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.setAudioPermission(audioPermission)

        // ✅ NEW: Check camera permission
        val cameraPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.setCameraPermission(cameraPermission)
    }

    private fun requestAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.setAudioPermission(true)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                Toast.makeText(
                    this,
                    "Audio permission is needed to record your voice responses",
                    Toast.LENGTH_LONG
                ).show()
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            else -> {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    // ✅ NEW: Request camera permission for gaze detection
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.setCameraPermission(true)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this,
                    "Camera permission is needed for gaze detection during the game",
                    Toast.LENGTH_LONG
                ).show()
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}