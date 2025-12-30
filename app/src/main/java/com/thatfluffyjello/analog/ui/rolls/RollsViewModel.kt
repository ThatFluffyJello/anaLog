package com.thatfluffyjello.analog.ui.rolls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.data.Camera
import com.thatfluffyjello.analog.data.FilmRoll
import com.thatfluffyjello.analog.data.PhotoLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RollsViewModel(private val repository: AnalogRepository) : ViewModel() {

    // Data class to hold roll + preview data
    data class RollPreviewData(
        val roll: FilmRoll,
        val recentLogs: List<PhotoLog>,
        val totalCount: Int
    )

    val activeRolls: StateFlow<List<FilmRoll>> = repository.activeRolls
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Flow of active rolls with their previews and counts
    val activeRollsWithPreviews: StateFlow<List<RollPreviewData>> = repository.activeRolls
        .flatMapLatest { rolls ->
            if (rolls.isEmpty()) {
                flowOf(emptyList())
            } else {
                val flows = rolls.map { roll ->
                    repository.getLogsForRoll(roll.id).map { logs ->
                        RollPreviewData(
                            roll = roll,
                            recentLogs = logs.take(4), // Preview last 4
                            totalCount = logs.size
                        )
                    }
                }
                kotlinx.coroutines.flow.combine(flows) { it.toList() }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allLogs: StateFlow<List<PhotoLog>> = repository.allLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allCameras: StateFlow<List<Camera>> = repository.allCameras
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Data for Camera-centric view
    data class CameraRollState(
        val camera: Camera,
        val activeRoll: FilmRoll?,
        val currentFrameCount: Int
    )

    val cameraStates: StateFlow<List<CameraRollState>> = kotlinx.coroutines.flow.combine(
        repository.allCameras,
        repository.activeRolls
    ) { cameras, rolls ->
        cameras.map { camera ->
            val roll = rolls.find { it.cameraId == camera.id }
            val count = if (roll != null) {
                // We need to fetch count. This is tricky in a map. 
                // Ideally we shouldn't do DB calls inside map.
                // For now, let's just use the `activeRollsWithPreviews` logic or similar.
                // Or better, let `activeRollsWithPreviews` be the source of truth for counts.
                0 // Placeholder, see logic below
            } else 0
            
            // Note: This simple combine doesn't easily support async fetching for each item.
            // We need a `flatMapLatest` approach similar to activeRollsWithPreviews.
            CameraRollState(camera, roll, 0)
        }
    }
    .flatMapLatest { initialStates ->
        if (initialStates.isEmpty()) flowOf(emptyList())
        else {
             val flows = initialStates.map { state ->
                 if (state.activeRoll != null) {
                     repository.getLogsForRoll(state.activeRoll.id).map { logs ->
                         state.copy(currentFrameCount = logs.size)
                     }
                 } else {
                     flowOf(state)
                 }
             }
             kotlinx.coroutines.flow.combine(flows) { it.toList() }
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createRoll(
        name: String,
        camera: String,
        lens: String,
        film: String,
        iso: String,
        cameraId: Int? = null // Optional binding
    ) {
        viewModelScope.launch {
            val roll = FilmRoll(
                name = name,
                cameraModel = camera,
                lensModel = lens,
                filmStock = film,
                iso = iso.toIntOrNull() ?: 0,
                isActive = true,
                cameraId = cameraId
            )
            repository.createRoll(roll)
        }
    }
}
