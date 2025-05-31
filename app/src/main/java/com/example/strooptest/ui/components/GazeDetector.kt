// File: ui/components/GazeDetector.kt
package com.example.strooptest.ui.components

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.Face
import com.example.strooptest.data.GazeDetectionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GazeDetector(
    private val onGazeDetected: (GazeDetectionData) -> Unit,
    private val gameSessionId: String,
    private val getCurrentGameContext: () -> Triple<String, String, Boolean> // question, answer, isCorrect
) : ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
    )

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(image)
                .addOnSuccessListener { faces ->
                    processFaces(faces, imageProxy.width, imageProxy.height)
                }
                .addOnFailureListener { e ->
                    Log.e("GazeDetector", "Face detection failed", e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun processFaces(faces: List<Face>, imageWidth: Int, imageHeight: Int) {
        if (faces.isNotEmpty()) {
            val face = faces.first() // Use the first detected face
            val gameContext = getCurrentGameContext()

            // Extract eye data
            val leftEye = face.getLandmark(com.google.mlkit.vision.face.FaceLandmark.LEFT_EYE)
            val rightEye = face.getLandmark(com.google.mlkit.vision.face.FaceLandmark.RIGHT_EYE)

            // Calculate gaze estimation (simplified approach)
            val gazeX = estimateGazeX(face, imageWidth)
            val gazeY = estimateGazeY(face, imageHeight)

            val gazeData = GazeDetectionData(
                gameSessionId = gameSessionId,

                // Eye detection
                leftEyeOpenProbability = face.leftEyeOpenProbability ?: 0f,
                rightEyeOpenProbability = face.rightEyeOpenProbability ?: 0f,
                leftEyeX = leftEye?.position?.x ?: 0f,
                leftEyeY = leftEye?.position?.y ?: 0f,
                rightEyeX = rightEye?.position?.x ?: 0f,
                rightEyeY = rightEye?.position?.y ?: 0f,

                // Head pose (limbs/head position)
                faceRotationY = face.headEulerAngleY,
                faceRotationX = face.headEulerAngleX,
                faceRotationZ = face.headEulerAngleZ,

                // Estimated gaze direction
                estimatedGazeX = gazeX,
                estimatedGazeY = gazeY,

                // Head bounds
                headCenterX = face.boundingBox.centerX().toFloat(),
                headCenterY = face.boundingBox.centerY().toFloat(),
                headWidth = face.boundingBox.width().toFloat(),
                headHeight = face.boundingBox.height().toFloat(),

                // Game context
                currentQuestion = gameContext.first,
                correctAnswer = gameContext.second,
                isCorrectResponse = gameContext.third
            )

            CoroutineScope(Dispatchers.Main).launch {
                onGazeDetected(gazeData)
            }
        }
    }

    private fun estimateGazeX(face: Face, imageWidth: Int): Float {
        // Simple gaze estimation based on head rotation and eye position
        val headRotationY = face.headEulerAngleY
        val faceCenter = face.boundingBox.centerX().toFloat()

        // Normalize to screen coordinates (-1 to 1, where 0 is center)
        val normalizedFaceX = (faceCenter / imageWidth) * 2 - 1
        val rotationFactor = headRotationY / 90f // Normalize rotation

        return (normalizedFaceX + rotationFactor).coerceIn(-1f, 1f)
    }

    private fun estimateGazeY(face: Face, imageHeight: Int): Float {
        // Simple gaze estimation based on head pitch and eye position
        val headRotationX = face.headEulerAngleX
        val faceCenter = face.boundingBox.centerY().toFloat()

        // Normalize to screen coordinates (-1 to 1, where 0 is center)
        val normalizedFaceY = (faceCenter / imageHeight) * 2 - 1
        val rotationFactor = headRotationX / 90f

        return (normalizedFaceY + rotationFactor).coerceIn(-1f, 1f)
    }

    fun release() {
        detector.close()
    }
}