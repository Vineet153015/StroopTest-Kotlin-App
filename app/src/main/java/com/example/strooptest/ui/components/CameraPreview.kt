// File: ui/components/CameraPreview.kt
package com.example.strooptest.ui.components

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.strooptest.data.GazeDetectionData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onGazeDetected: (GazeDetectionData) -> Unit,
    gameSessionId: String,
    getCurrentGameContext: () -> Triple<String, String, Boolean>,
    isVisible: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var gazeDetector by remember { mutableStateOf<GazeDetector?>(null) }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            gazeDetector?.release()
        }
    }

    LaunchedEffect(gameSessionId) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProvider = cameraProviderFuture.get()
    }

    if (isVisible && cameraProvider != null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.TopEnd
        ) {
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        setupCamera(
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            cameraProvider = cameraProvider!!,
                            cameraExecutor = cameraExecutor,
                            onGazeDetected = onGazeDetected,
                            gameSessionId = gameSessionId,
                            getCurrentGameContext = getCurrentGameContext
                        ) { detector ->
                            gazeDetector = detector
                        }
                    }
                },
                modifier = Modifier
                    .size(120.dp) // Small preview in corner
                    .clip(CircleShape)
            )
        }
    }
}

private fun PreviewView.setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraProvider: ProcessCameraProvider,
    cameraExecutor: ExecutorService,
    onGazeDetected: (GazeDetectionData) -> Unit,
    gameSessionId: String,
    getCurrentGameContext: () -> Triple<String, String, Boolean>,
    onDetectorCreated: (GazeDetector) -> Unit
) {
    try {
        // Unbind all use cases before rebinding
        cameraProvider.unbindAll()

        // Preview use case
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(surfaceProvider)

        // Image analysis use case for gaze detection
        val gazeDetector = GazeDetector(
            onGazeDetected = onGazeDetected,
            gameSessionId = gameSessionId,
            getCurrentGameContext = getCurrentGameContext
        )

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(android.util.Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor, gazeDetector)

        // Camera selector (front-facing for gaze detection)
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        // Bind use cases to camera
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )

        onDetectorCreated(gazeDetector)

    } catch (e: Exception) {
        Log.e("CameraPreview", "Camera setup failed", e)
    }
}