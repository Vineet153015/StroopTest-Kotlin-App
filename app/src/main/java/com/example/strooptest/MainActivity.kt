package com.example.strooptest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strooptest.ui.navigation.StroopNavigation
import com.example.strooptest.ui.theme.StroopTestTheme
import com.example.strooptest.viewmodel.StroopGameViewModel

class MainActivity : ComponentActivity() {

    private var hasAudioPermission by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAudioPermission()

        setContent {
            StroopTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: StroopGameViewModel = viewModel()

                    LaunchedEffect(hasAudioPermission) {
                        viewModel.setAudioPermission(hasAudioPermission)
                    }

                    StroopNavigation(
                        viewModel = viewModel,
                        onRequestPermission = { requestAudioPermission() }
                    )
                }
            }
        }
    }

    private fun checkAudioPermission() {
        hasAudioPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}