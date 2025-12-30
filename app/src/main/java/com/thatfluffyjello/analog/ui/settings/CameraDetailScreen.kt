package com.thatfluffyjello.analog.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailScreen(
    factory: ViewModelFactory,
    onBack: () -> Unit,
    cameraId: Int? = null // Null for new camera
) {
    val viewModel: SettingsViewModel = viewModel(factory = factory)
    
    // State
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("35mm") } // Default
    var year by remember { mutableStateOf("") }
    var lenses by remember { mutableStateOf("") }
    
    // Load data if editing existing camera
    // Normally we'd fetch asynchronously, but for simplicity assuming we might pass data or fetch in LaunchedEffect.
    // Given SettingsViewModel holds allCameras, we can find it there.
    val allCameras by viewModel.allCameras.collectAsState()
    
    LaunchedEffect(cameraId) {
        if (cameraId != null && cameraId != -1) {
            val camera = allCameras.find { it.id == cameraId }
            if (camera != null) {
                name = camera.name
                description = camera.description
                format = camera.format
                year = camera.year?.toString() ?: ""
                lenses = camera.lenses
            }
        }
    }

    val formats = listOf("35mm", "120mm", "Half Frame", "16mm")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (cameraId == null) "Add Camera" else "Edit Camera") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                if (cameraId == null || cameraId == -1) {
                                    viewModel.addCamera(
                                        name = name,
                                        description = description,
                                        format = format,
                                        year = year.toIntOrNull(),
                                        lenses = lenses
                                    )
                                } else {
                                    // Update logic needs to be added to ViewModel
                                     viewModel.updateCamera(
                                        id = cameraId,
                                        name = name,
                                        description = description,
                                        format = format,
                                        year = year.toIntOrNull(),
                                        lenses = lenses
                                    )
                                }
                                onBack()
                            }
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Basic Info
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Model Name") },
                placeholder = { Text("e.g. Canon AE-1") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { c -> c.isDigit() } },
                    label = { Text("Year") },
                    placeholder = { Text("1976") },
                    modifier = Modifier.weight(0.4f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                // Format Selector (Dropdown or Segmented Button equivalent)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(0.6f)
                ) {
                    OutlinedTextField(
                        value = format,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Format") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        formats.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection) },
                                onClick = {
                                    format = selection
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description / Notes") },
                placeholder = { Text("Silver body, light leak on hinge...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            // Lenses
            OutlinedTextField(
                value = lenses,
                onValueChange = { lenses = it },
                label = { Text("Lenses") },
                placeholder = { Text("50mm f/1.8, 28mm f/2.8") },
                supportingText = { Text("Comma separated list") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
