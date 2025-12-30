package com.thatfluffyjello.analog.data

import kotlinx.coroutines.flow.Flow

class AnalogRepository(
    private val photoLogDao: PhotoLogDao,
    private val filmRollDao: FilmRollDao,
    private val cameraDao: CameraDao
) {
    val allLogs: Flow<List<PhotoLog>> = photoLogDao.getAllLogs()
    val activeRolls: Flow<List<FilmRoll>> = filmRollDao.getActiveRolls()
    val allCameras: Flow<List<Camera>> = cameraDao.getAllCameras()

    suspend fun getLog(id: Int): PhotoLog? = photoLogDao.getLogById(id)
    suspend fun addLog(log: PhotoLog) = photoLogDao.insertLog(log)
    suspend fun updateLog(log: PhotoLog) = photoLogDao.updateLog(log)
    suspend fun deleteLog(log: PhotoLog) = photoLogDao.deleteLog(log)

    suspend fun getRoll(id: Int): FilmRoll? = filmRollDao.getRollById(id)

    suspend fun createRoll(roll: FilmRoll) {
        filmRollDao.insertRoll(roll)
    }
    
    fun getLogsForRoll(rollId: Int) = filmRollDao.getLogsForRoll(rollId)

    // Camera Methods
    suspend fun addCamera(camera: Camera) = cameraDao.insertCamera(camera)
    suspend fun updateCamera(camera: Camera) = cameraDao.updateCamera(camera)
    suspend fun deleteCamera(camera: Camera) = cameraDao.deleteCamera(camera)
}
