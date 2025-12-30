package com.thatfluffyjello.analog

import android.app.Application
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.data.AppDatabase

class AnalogApplication : Application() {
    // Manual Dependency Injection
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { 
        AnalogRepository(
            database.photoLogDao(), 
            database.filmRollDao(),
            database.cameraDao()
        ) 
    }
}
