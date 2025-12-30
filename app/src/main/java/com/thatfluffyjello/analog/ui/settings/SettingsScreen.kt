package com.thatfluffyjello.analog.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatfluffyjello.analog.data.AnalogRepository
import com.thatfluffyjello.analog.data.Camera
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: AnalogRepository) : ViewModel() {
    val allCameras: StateFlow<List<Camera>> = repository.allCameras
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCamera(name: String, description: String, format: String, year: Int?, lenses: String) {
        viewModelScope.launch {
            repository.addCamera(Camera(
                name = name, 
                description = description, 
                format = format,
                year = year,
                lenses = lenses
            ))
        }
    }

    fun updateCamera(id: Int, name: String, description: String, format: String, year: Int?, lenses: String) {
        viewModelScope.launch {
            repository.updateCamera(Camera(
                id = id,
                name = name, 
                description = description, 
                format = format,
                year = year,
                lenses = lenses
            ))
        }
    }

    fun deleteCamera(camera: Camera) {
        viewModelScope.launch {
            repository.deleteCamera(camera)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    factory: ViewModelFactory,
    onBack: () -> Unit,
    onAddCamera: () -> Unit,
    onEditCamera: (Int) -> Unit
) {
    val viewModel: SettingsViewModel = viewModel(factory = factory)
    val cameras by viewModel.allCameras.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gear Locker") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCamera) {
                Icon(Icons.Default.Add, "Add Camera")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cameras) { camera ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditCamera(camera.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(camera.name, style = MaterialTheme.typography.titleMedium)
                            Text("${camera.format} • ${camera.description}", style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { viewModel.deleteCamera(camera) }) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                }
            }
            if (cameras.isEmpty()) {
                item {
                    Text(
                        "No cameras added yet. Tap + to add your gear.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AddCameraDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("35mm") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Camera") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Camera Name") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Lens / Desc") })
                // Format Selector could be dropdown, for now text
                OutlinedTextField(value = format, onValueChange = { format = it }, label = { Text("Format (35mm, 120...)") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, description, format) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
