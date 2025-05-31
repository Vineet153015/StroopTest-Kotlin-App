
package com.example.strooptest.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.Database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import android.content.Context

@Entity(tableName = "gaze_detection_data")
data class GazeDetectionData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val gameSessionId: String,

    // Eye detection data
    val leftEyeOpenProbability: Float = 0f,
    val rightEyeOpenProbability: Float = 0f,
    val leftEyeX: Float = 0f,
    val leftEyeY: Float = 0f,
    val rightEyeX: Float = 0f,
    val rightEyeY: Float = 0f,

    // Facial landmarks for gaze direction
    val faceRotationY: Float = 0f, // Head yaw
    val faceRotationX: Float = 0f, // Head pitch
    val faceRotationZ: Float = 0f, // Head roll

    // Pupil estimation (calculated from eye landmarks)
    val estimatedGazeX: Float = 0f,
    val estimatedGazeY: Float = 0f,

    // Head pose (limbs - neck/head position)
    val headCenterX: Float = 0f,
    val headCenterY: Float = 0f,
    val headWidth: Float = 0f,
    val headHeight: Float = 0f,

    // Game context
    val currentQuestion: String = "",
    val correctAnswer: String = "",
    val isCorrectResponse: Boolean = false
)

@Dao
interface GazeDetectionDao {
    @Insert
    suspend fun insertGazeData(gazeData: GazeDetectionData)

    @Query("SELECT * FROM gaze_detection_data WHERE gameSessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getGazeDataForSession(sessionId: String): List<GazeDetectionData>

    @Query("SELECT * FROM gaze_detection_data ORDER BY timestamp DESC")
    suspend fun getAllGazeData(): List<GazeDetectionData>

    @Query("DELETE FROM gaze_detection_data")
    suspend fun clearAllData()
}

@Database(
    entities = [GazeDetectionData::class],
    version = 1,
    exportSchema = false
)
abstract class GazeDetectionDatabase : androidx.room.RoomDatabase() {
    abstract fun gazeDao(): GazeDetectionDao

    companion object {
        @Volatile
        private var INSTANCE: GazeDetectionDatabase? = null

        fun getDatabase(context: Context): GazeDetectionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GazeDetectionDatabase::class.java,
                    "gaze_detection_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}