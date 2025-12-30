package com.thatfluffyjello.analog.ui.editor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLogScreen(
    factory: ViewModelFactory,
    logId: Int,
    imageUri: String?,
    rollId: Int = -1, // Default to -1 (no specific roll)
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: EditLogViewModel = viewModel(factory = factory)
    // Observe active rolls for the dropdown
    val activeRolls by viewModel.activeRolls.collectAsState()
    
    LaunchedEffect(logId, imageUri) {
        if (logId != -1) {
            viewModel.loadLog(logId)
        } else {
            // Pass rollId if it's a valid ID (different from -1)
            viewModel.initNewLog(imageUri, if (rollId != -1) rollId else null)
        }
    }

    val state = viewModel.uiState
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context) { lat, lon ->
                viewModel.updateLocation(lat, lon)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (logId == -1) "New Log" else "Edit Log") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.saveLog(onSaved = onSave) },
                icon = { Icon(Icons.Default.Check, contentDescription = "Save") },
                text = { Text("Save") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.imagePath.isNotEmpty()) {
                AsyncImage(
                    model = state.imagePath,
                    contentDescription = "Reference",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            // Roll Selection (Dropdown)
            var expanded by remember { mutableStateOf(false) }
            val selectedRollName = activeRolls.find { it.id == state.rollId }?.name ?: "Select Roll"

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = selectedRollName,
                    onValueChange = {},
                    label = { Text("Active Roll") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (roll in activeRolls) {
                        DropdownMenuItem(
                            text = { 
                                Column {
                                    Text(roll.name)
                                    Text("${roll.cameraModel} • ${roll.filmStock}", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                viewModel.updateRoll(roll.id)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }


            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.latitude != null && state.longitude != null) {
                    Text("Loc: ${state.latitude}, ${state.longitude}", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("No Location", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                         getCurrentLocation(context) { lat, lon -> viewModel.updateLocation(lat, lon) }
                    } else {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Get Location")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.cameraModel,
                    onValueChange = { viewModel.updateCamera(it) },
                    label = { Text("Camera") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.lensModel,
                    onValueChange = { viewModel.updateLens(it) },
                    label = { Text("Lens") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = state.filmStock,
                onValueChange = { viewModel.updateFilm(it) },
                label = { Text("Film Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = if (state.iso > 0) state.iso.toString() else "",
                    onValueChange = { viewModel.updateIso(it) },
                    label = { Text("ISO") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.aperture,
                    onValueChange = { viewModel.updateAperture(it) },
                    label = { Text("Aperture") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.shutterSpeed,
                    onValueChange = { viewModel.updateShutter(it) },
                    label = { Text("Shutter") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = state.notes,
                onValueChange = { viewModel.updateNotes(it) },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(64.dp)) // Spacing for FAB
        }
    }
}

private fun getCurrentLocation(context: Context, onLocation: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                onLocation(it.latitude, it.longitude)
            }
        }
    } catch (e: SecurityException) {
        // Handle exception
    }
}
