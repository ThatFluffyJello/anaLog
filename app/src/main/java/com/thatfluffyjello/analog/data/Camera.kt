package com.thatfluffyjello.analog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cameras")
data class Camera(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,     // e.g. "Canon AE-1"
    val description: String = "", // e.g. "SLR with 50mm"
    val type: String = "SLR", // SLR, Rangefinder, Point & Shoot, 120, etc.
    val format: String = "35mm", // 35mm, 120, etc.
    val year: Int? = null,
    val lenses: String = "" // Comma-delimited list of lenses
)
