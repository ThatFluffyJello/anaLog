package com.thatfluffyjello.analog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotoLog::class, FilmRoll::class, Camera::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoLogDao(): PhotoLogDao
    abstract fun filmRollDao(): FilmRollDao
    abstract fun cameraDao(): CameraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "analog_database"
                )
                .fallbackToDestructiveMigration() // Destructive for dev simplicity
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
