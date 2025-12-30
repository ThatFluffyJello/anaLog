package com.thatfluffyjello.analog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film_rolls")
data class FilmRoll(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "", // e.g. "Summer Trip 2024"
    val cameraModel: String = "", // Snapshot of name
    val lensModel: String = "", // Snapshot of lens
    val filmStock: String = "",
    val iso: Int = 0,
    val dateStarted: Long = System.currentTimeMillis(),
    val dateCompleted: Long? = null,
    val isActive: Boolean = true,
    val cameraId: Int? = null, // Link to Camera Inventory
    val exposures: Int = 36 // Max exposures
)
