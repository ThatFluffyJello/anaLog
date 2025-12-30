package com.thatfluffyjello.analog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.ui.home.HomeViewModel

class ViewModelFactory(private val repository: AnalogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(com.thatfluffyjello.analog.ui.editor.EditLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.thatfluffyjello.analog.ui.editor.EditLogViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(com.thatfluffyjello.analog.ui.rolls.RollsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.thatfluffyjello.analog.ui.rolls.RollsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(com.thatfluffyjello.analog.ui.settings.SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.thatfluffyjello.analog.ui.settings.SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
