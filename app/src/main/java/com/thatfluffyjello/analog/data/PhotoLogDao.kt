package com.thatfluffyjello.analog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoLogDao {
    @Query("SELECT * FROM photo_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<PhotoLog>>

    @Query("SELECT * FROM photo_logs WHERE id = :id")
    suspend fun getLogById(id: Int): PhotoLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PhotoLog)

    @Update
    suspend fun updateLog(log: PhotoLog)

    @Delete
    suspend fun deleteLog(log: PhotoLog)
}
