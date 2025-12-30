package com.thatfluffyjello.analog.ui.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.data.PhotoLog
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditLogViewModel(private val repository: AnalogRepository) : ViewModel() {

    var uiState by mutableStateOf(PhotoLog(timestamp = System.currentTimeMillis(), imagePath = ""))
        private set

    // Expose active rolls for the selector
    val activeRolls = repository.activeRolls.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun loadLog(id: Int) {
        if (id == -1) return // New log
        viewModelScope.launch {
            repository.getLog(id)?.let {
                uiState = it
            }
        }
    }

    fun initNewLog(imageUri: String?, rollId: Int? = null) {
        if (uiState.id == 0 && imageUri != null) {
            viewModelScope.launch {
                val activeRoll = if (rollId != null) repository.getRoll(rollId) else null
                
                uiState = uiState.copy(
                    imagePath = imageUri,
                    timestamp = System.currentTimeMillis(),
                    cameraModel = activeRoll?.cameraModel ?: "",
                    lensModel = activeRoll?.lensModel ?: "",
                    filmStock = activeRoll?.filmStock ?: "",
                    iso = activeRoll?.iso ?: 0,
                    rollId = activeRoll?.id
                )
            }
        }
    }

    fun updateRoll(rollId: Int) {
        viewModelScope.launch {
            val roll = repository.getRoll(rollId)
            if (roll != null) {
                uiState = uiState.copy(
                    rollId = roll.id,
                    cameraModel = roll.cameraModel,
                    lensModel = roll.lensModel,
                    filmStock = roll.filmStock,
                    iso = roll.iso
                )
            }
        }
    }

    fun updateCamera(value: String) { uiState = uiState.copy(cameraModel = value) }
    fun updateLens(value: String) { uiState = uiState.copy(lensModel = value) }
    fun updateFilm(value: String) { uiState = uiState.copy(filmStock = value) }
    fun updateIso(value: String) { 
        uiState = uiState.copy(iso = value.toIntOrNull() ?: 0) 
    }
    fun updateAperture(value: String) { uiState = uiState.copy(aperture = value) }
    fun updateShutter(value: String) { uiState = uiState.copy(shutterSpeed = value) }
    fun updateNotes(value: String) { uiState = uiState.copy(notes = value) }
    fun updateLocation(lat: Double, lon: Double) {
        uiState = uiState.copy(latitude = lat, longitude = lon)
    }

    fun saveLog(onSaved: () -> Unit) {
        viewModelScope.launch {
            if (uiState.id == 0) {
                repository.addLog(uiState)
            } else {
                repository.updateLog(uiState)
            }
            onSaved()
        }
    }
}
