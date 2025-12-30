package com.thatfluffyjello.analog.ui.rolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRollScreen(
    factory: ViewModelFactory,
    onBack: () -> Unit,
    onRollCreated: () -> Unit
) {
    val viewModel: RollsViewModel = viewModel(factory = factory)
    
    val allCameras by viewModel.allCameras.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var selectedCamera by remember { mutableStateOf<com.thatfluffyjello.analog.data.Camera?>(null) }
    var lens by remember { mutableStateOf("") } // Maybe auto-fill from camera?
    var film by remember { mutableStateOf("") }
    var iso by remember { mutableStateOf("") }
    
    var cameraExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCamera) {
        selectedCamera?.let {
            if (lens.isBlank()) lens = it.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Start New Roll") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Load film into one of your cameras.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Camera Selector
            androidx.compose.material3.ExposedDropdownMenuBox(
                expanded = cameraExpanded,
                onExpandedChange = { cameraExpanded = !cameraExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCamera?.name ?: "Select Camera",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Camera") },
                    trailingIcon = { androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon(expanded = cameraExpanded) },
                    colors = androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = cameraExpanded,
                    onDismissRequest = { cameraExpanded = false }
                ) {
                    if (allCameras.isEmpty()) {
                         androidx.compose.material3.DropdownMenuItem(
                            text = { Text("No cameras found. Add one in Settings!") },
                            onClick = { cameraExpanded = false }
                        )
                    } else {
                        allCameras.forEach { camera ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text(camera.name) },
                                onClick = {
                                    selectedCamera = camera
                                    cameraExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Roll Name (e.g., Summer Trip)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = film,
                onValueChange = { film = it },
                label = { Text("Film Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = iso,
                    onValueChange = { iso = it },
                    label = { Text("ISO") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(0.5f)
                )
                 OutlinedTextField(
                    value = lens,
                    onValueChange = { lens = it },
                    label = { Text("Lens") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Button(
                onClick = {
                    viewModel.createRoll(
                        name = name,
                        camera = selectedCamera?.name ?: "Unknown",
                        lens = lens,
                        film = film,
                        iso = iso,
                        cameraId = selectedCamera?.id
                    )
                    onRollCreated()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && film.isNotBlank() && selectedCamera != null
            ) {
                Text("Load Film")
            }
        }
    }
}
