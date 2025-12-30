package com.thatfluffyjello.analog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.data.PhotoLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(repository: AnalogRepository) : ViewModel() {
    val logs: StateFlow<List<PhotoLog>> = repository.allLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
