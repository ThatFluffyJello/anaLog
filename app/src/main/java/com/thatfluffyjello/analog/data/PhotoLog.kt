package com.thatfluffyjello.analog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_logs")
data class PhotoLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val imagePath: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val cameraModel: String = "",
    val lensModel: String = "",
    val filmStock: String = "",
    val iso: Int = 0,
    val aperture: String = "",
    val shutterSpeed: String = "",
    val notes: String = "",
    val rollId: Int? = null // Nullable for compatibility/standalone logs
)
