package com.thatfluffyjello.analog.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatfluffyjello.analog.ui.rolls.RollsViewModel
import com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

import android.media.MediaActionSound
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer

enum class AspectRatioMode(val label: String, val ratio: Float) {
    Ratio35mm("35mm", 2f / 3f),
    RatioHalfFrame("Half 35", 3f / 4f),
    Ratio120mm("120mm", 1f),
    Ratio16mm("16mm", 4f / 3f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    factory: ViewModelFactory,
    rollId: Int,
    onImageCaptured: (Uri, Int) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val viewModel: RollsViewModel = viewModel(factory = factory)
    val cameraStates by viewModel.cameraStates.collectAsState()
    
    // We enter with a rollId. We need to find which camera corresponds to it.
    // Or if rollId is -1, select first available camera.
    var currentCameraId by remember { mutableIntStateOf(-1) }
    
    LaunchedEffect(cameraStates, rollId) {
        if (currentCameraId == -1 && cameraStates.isNotEmpty()) {
            if (rollId != -1) {
                val stateWithRoll = cameraStates.find { it.activeRoll?.id == rollId }
                if (stateWithRoll != null) {
                    currentCameraId = stateWithRoll.camera.id
                    return@LaunchedEffect
                }
            }
            // Default to first camera
            currentCameraId = cameraStates.first().camera.id
        }
    }

    val currentCameraState = cameraStates.find { it.camera.id == currentCameraId }
    val currentRoll = currentCameraState?.activeRoll
    val frameCount = currentCameraState?.currentFrameCount ?: 0

    var aspectRatioMode by remember { mutableStateOf(AspectRatioMode.Ratio35mm) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        // ... (Preview initialization omitted for brevity, it remains same) ...
        val previewView = remember { PreviewView(context) }
        val imageCapture = remember { ImageCapture.Builder().build() }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        LaunchedEffect(cameraSelector) {
            val cameraProvider = context.getCameraProvider()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector,
                    previewView.surfaceProvider?.let {
                         val preview = Preview.Builder().build()
                         preview.setSurfaceProvider(it)
                         preview
                    },
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraScreen", "UseCase binding failed", e)
            }
        }

        Scaffold(
            containerColor = Color(0xFF191C20),
            contentColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // --- TOP INFO ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                ) {
                    if (currentRoll != null) {
                        Text(
                            text = "ISO ${currentRoll.iso} | ${currentRoll.filmStock} | frame ${frameCount + 1}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    } else {
                        Text("No Film Loaded", color = Color.Gray)
                    }
                }

    // Sound
    val mediaActionSound = remember { MediaActionSound() }
    LaunchedEffect(Unit) {
        mediaActionSound.load(MediaActionSound.SHUTTER_CLICK)
    }

    // Flash Animation State
    var showFlash by remember { mutableStateOf(false) }
    val flashAlpha by animateFloatAsState(
        targetValue = if (showFlash) 1f else 0f,
        animationSpec = tween(100),
        finishedListener = { showFlash = false }
    )
    
                // --- PREVIEW ---
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatioMode.ratio)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black)
                            .animateContentSize()
                    ) {
                        AndroidView(
                            factory = { previewView },
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Grid Overlay
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val oneThirdW = width / 3f
                            val twoThirdsW = oneThirdW * 2
                            val oneThirdH = height / 3f
                            val twoThirdsH = oneThirdH * 2
                            
                            val lineColor = Color.White.copy(alpha = 0.3f)
                            val strokeWidth = 1.dp.toPx()

                            // Vertical lines
                            drawLine(lineColor, Offset(oneThirdW, 0f), Offset(oneThirdW, height), strokeWidth)
                            drawLine(lineColor, Offset(twoThirdsW, 0f), Offset(twoThirdsW, height), strokeWidth)
                            
                            // Horizontal lines
                            drawLine(lineColor, Offset(0f, oneThirdH), Offset(width, oneThirdH), strokeWidth)
                            drawLine(lineColor, Offset(0f, twoThirdsH), Offset(width, twoThirdsH), strokeWidth)
                        }

                        // Flash Overlay
                        if (flashAlpha > 0f) {
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = flashAlpha)))
                        }
                    }
                }

                // --- CONTROLS SECTION ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Aspect Ratio Pill
                    Surface(
                        color = Color(0xFFE8DEF8),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .height(48.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AspectRatioMode.values().forEach { mode ->
                                val isSelected = aspectRatioMode == mode
                                val backgroundColor by animateColorAsState(
                                    if (isSelected) Color(0xFFD0BCFF) else Color.Transparent,
                                    label = "ratioBg"
                                )
                                val textColor by animateColorAsState(
                                    if (isSelected) Color.Black else Color.Black.copy(alpha = 0.6f),
                                    label = "ratioText"
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { aspectRatioMode = mode }
                                        .background(backgroundColor)
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = mode.label,
                                        color = textColor,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Camera Selector Chips (Centered)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                         LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(cameraStates) { state ->
                                val isSelected = state.camera.id == currentCameraId
                                val chipColor by animateColorAsState(
                                    if (isSelected) Color(0xFF4A4458) else Color(0xFFD0BCFF),
                                    label = "rollChip"
                                )
                                val textColor by animateColorAsState(
                                    if (isSelected) Color.White else Color.Black,
                                    label = "rollText"
                                )

                                Surface(
                                    onClick = { currentCameraId = state.camera.id },
                                    shape = RoundedCornerShape(12.dp),
                                    color = chipColor,
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = state.camera.name,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = textColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Shutter Button (Animated & Round)
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        if (isPressed) 0.9f else 1f,
                        label = "shutterScale"
                    )

                    FilledIconButton(
                        onClick = {
                            if (currentRoll != null) {
                                mediaActionSound.play(MediaActionSound.SHUTTER_CLICK)
                                showFlash = true // Trigger flash
                                takePhoto(
                                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                    imageCapture = imageCapture,
                                    outputDirectory = context.getOutputDirectory(),
                                    executor = ContextCompat.getMainExecutor(context),
                                    onImageCaptured = { uri -> onImageCaptured(uri, currentRoll.id) },
                                    onError = { Log.e("CameraScreen", "Capture failed: $it") }
                                )
                            } else {
                                // Maybe show toast "Load film first"
                            }
                        },
                        modifier = Modifier
                            .size(84.dp) // Slightly larger
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        shape = CircleShape, // Round!
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFFE8DEF8)),
                        interactionSource = interactionSource
                    ) {
                        Canvas(modifier = Modifier.size(40.dp)) {
                            drawCircle(
                                color = Color.Black,
                                style = Stroke(width = 3.dp.toPx())
                            )
                            drawCircle(
                                color = Color.Black,
                                radius = size.minDimension / 2.8f
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
             Text("Camera permission required", color = Color.White)
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        continuation.resume(cameraProviderFuture.get())
    }, ContextCompat.getMainExecutor(this))
}

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onImageCaptured(savedUri)
            }
        }
    )
}

private fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, "AnaLog").apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}
