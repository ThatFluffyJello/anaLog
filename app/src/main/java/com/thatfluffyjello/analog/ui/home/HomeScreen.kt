package com.thatfluffyjello.analog.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.foundation.background
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.thatfluffyjello.analog.data.PhotoLog
import com.thatfluffyjello.analog.ui.rolls.RollsViewModel
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    factory: ViewModelFactory,
    onAddClick: (Int) -> Unit,
    onNewRollClick: () -> Unit,
    onLogClick: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {
    val viewModel: RollsViewModel = viewModel(factory = factory)
    // Combined list of roll data (Roll + Recent Logs + Count)
    val activeRollsData by viewModel.activeRollsWithPreviews.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()

    var currentScreen by remember { mutableIntStateOf(0) } // 0 = Home, 1 = Library

    // Aesthetics based on "Music App" style
    val backgroundColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val primaryColor = Color(0xFFD0BCFF) 

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = backgroundColor,
            surface = surfaceColor,
            primary = primaryColor,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF1E1E1E),
                    contentColor = Color.White
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text("Home") },
                        selected = currentScreen == 0,
                        onClick = { currentScreen = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.PhotoLibrary, "Library") },
                        label = { Text("Library") },
                        selected = currentScreen == 1,
                        onClick = { currentScreen = 1 }
                    )
                }
            },
            floatingActionButton = {
                // "Floating bar" / Button
                val canSnap = activeRollsData.isNotEmpty()
                ExtendedFloatingActionButton(
                    onClick = {
                        if (canSnap) {
                            // Default to first active roll for now if multiple, 
                            // user can also click specific header buttons.
                            // Ideally show chooser if > 1? For speed, top/most recent is likely best.
                            val topRoll = activeRollsData.first().roll
                            onAddClick(topRoll.id)
                        } else {
                            onNewRollClick()
                        }
                    },
                    containerColor = primaryColor,
                    contentColor = Color.Black,
                    icon = { Icon(if (canSnap) Icons.Default.CameraAlt else Icons.Default.Add, null) },
                    text = { Text(if (canSnap) "Snap" else "New Roll") }
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            if (currentScreen == 0) {
                // HOME: Vertical Stack of Active Rolls
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4), // 4 columns for small previews
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = innerPadding.calculateTopPadding(),
                        bottom = 100.dp 
                    ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(4) }) {
                        Text(
                            text = "Your\nRolls",
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White,
                            lineHeight = 48.sp,
                            modifier = Modifier.padding(bottom = 24.dp, top = 20.dp)
                        )
                    }

                    if (activeRollsData.isEmpty()) {
                        item(span = { GridItemSpan(4) }) {
                            Text("No active rolls. Start one!", color = Color.White.copy(alpha = 0.5f))
                        }
                    } else {
                        // Iterate through each active roll data
                        activeRollsData.forEach { data ->
                            // Header for the Roll
                            item(span = { GridItemSpan(4) }) {
                                RollHeaderSection(
                                    name = data.roll.name,
                                    details = "${data.roll.cameraModel} • ${data.roll.filmStock}",
                                    count = data.totalCount,
                                    onSnapClick = { onAddClick(data.roll.id) }
                                )
                            }
                            
                            // Previews (up to 4)
                            if (data.recentLogs.isEmpty()) {
                                item(span = { GridItemSpan(4) }) {
                                    Text(
                                        "No photos yet.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }
                            } else {
                                items(data.recentLogs) { log ->
                                    MiniPhotoCard(log, onClick = { onLogClick(log.id) })
                                }
                                // Fill remaining spans if not divisible perfectly? No, Grid handles it.
                                // But we want "Spacer" after the section.
                                item(span = { GridItemSpan(4) }) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                // LIBRARY: Full Gallery
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(
                        start = 2.dp, 
                        end = 2.dp, 
                        top = innerPadding.calculateTopPadding(), 
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(allLogs) { log ->
                        GalleryPhotoCard(log, onClick = { onLogClick(log.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun RollHeaderSection(
    name: String,
    details: String,
    count: Int,
    onSnapClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = details,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
             // Frame Counter Badge
            Text(
                text = "Frame $count", // 0-indexed count or 1-indexed? count is total so far. Next is count+1
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFD0BCFF),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp)
            )
            FilledIconButton(
                onClick = onSnapClick,
                modifier = Modifier.size(36.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF2A2A2A))
            ) {
                Icon(Icons.Default.PhotoCamera, "Snap", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun MiniPhotoCard(log: PhotoLog, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = log.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GalleryPhotoCard(log: PhotoLog, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .background(Color.DarkGray) // Placeholder
    ) {
        AsyncImage(
            model = log.imagePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
