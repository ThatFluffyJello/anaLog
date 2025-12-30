package com.thatfluffyjello.analog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmRollDao {
    @Query("SELECT * FROM film_rolls ORDER BY dateStarted DESC")
    fun getAllRolls(): Flow<List<FilmRoll>>

    @Query("SELECT * FROM film_rolls WHERE id = :id")
    suspend fun getRollById(id: Int): FilmRoll?

    @Query("SELECT * FROM film_rolls WHERE isActive = 1 ORDER BY dateStarted DESC")
    fun getActiveRolls(): Flow<List<FilmRoll>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoll(roll: FilmRoll)

    @Update
    suspend fun updateRoll(roll: FilmRoll)

    @Query("SELECT * FROM photo_logs WHERE rollId = :rollId ORDER BY timestamp DESC")
    fun getLogsForRoll(rollId: Int): Flow<List<PhotoLog>>
}
