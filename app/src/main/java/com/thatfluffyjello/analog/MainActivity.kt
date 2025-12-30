package com.thatfluffyjello.analog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.thatfluffyjello.analog.ui.theme.AnalogTheme
// Imports for screens will be added as I create them. For now leaving placeholders or commented out.
// import com.thatfluffyjello.analog.ui.home.HomeScreen
// import com.thatfluffyjello.analog.ui.camera.CameraScreen
// import com.thatfluffyjello.analog.ui.editor.EditLogScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnalogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnalogApp()
                }
            }
        }
    }
}


@Composable
fun AnalogApp() {
    val context = LocalContext.current
    val app = context.applicationContext as AnalogApplication
    val viewModelFactory = com.thatfluffyjello.analog.ui.viewmodel.ViewModelFactory(app.repository)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
             com.thatfluffyjello.analog.ui.home.HomeScreen(
                 factory = viewModelFactory,
                 onAddClick = { rollId -> navController.navigate("camera/$rollId") },
                 onNewRollClick = { navController.navigate("new_roll") },
                 onLogClick = { logId -> navController.navigate("editor?logId=$logId") },
                 onSettingsClick = { navController.navigate("settings") }
             )
        }
        composable("settings") {
            com.thatfluffyjello.analog.ui.settings.SettingsScreen(
                factory = viewModelFactory,
                onBack = { navController.popBackStack() },
                onAddCamera = { navController.navigate("camera_detail") },
                onEditCamera = { cameraId -> navController.navigate("camera_detail?cameraId=$cameraId") }
            )
        }
        composable(
            route = "camera_detail?cameraId={cameraId}",
            arguments = listOf(navArgument("cameraId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
             val cameraId = backStackEntry.arguments?.getInt("cameraId") ?: -1
             com.thatfluffyjello.analog.ui.settings.CameraDetailScreen(
                 factory = viewModelFactory,
                 onBack = { navController.popBackStack() },
                 cameraId = if (cameraId == -1) null else cameraId
             )
        }
        composable("new_roll") {
            com.thatfluffyjello.analog.ui.rolls.CreateRollScreen(
                factory = viewModelFactory,
                onBack = { navController.popBackStack() },
                onRollCreated = { 
                    navController.popBackStack() 
                }
            )
        }
        composable(
            route = "camera/{rollId}",
            arguments = listOf(navArgument("rollId") { type = NavType.IntType })
        ) { backStackEntry ->
            val rollId = backStackEntry.arguments?.getInt("rollId") ?: -1
            com.thatfluffyjello.analog.ui.camera.CameraScreen(
                factory = viewModelFactory,
                rollId = rollId,
                onImageCaptured = { uri, activeRollId ->
                    val encodedUri = java.net.URLEncoder.encode(uri.toString(), "UTF-8")
                    // Pass activeRollId (which might be different from original rollId) to editor
                    navController.navigate("editor?imageUri=$encodedUri&rollId=$activeRollId") {
                        popUpTo("home") 
                    }
                }
            )
        }
        composable(
            route = "editor?logId={logId}&imageUri={imageUri}&rollId={rollId}",
            arguments = listOf(
                navArgument("logId") { type = NavType.IntType; defaultValue = -1 },
                navArgument("imageUri") { type = NavType.StringType; nullable = true },
                navArgument("rollId") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val logId = backStackEntry.arguments?.getInt("logId") ?: -1
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            val rollId = backStackEntry.arguments?.getInt("rollId") ?: -1
            
            // We need to pass rollId to the EditLogScreen somehow, or have it get it from ViewModel
            // EditLogScreen takes `factory`. `EditLogViewModel` initialization needs `rollId` if it's new.
            // Screen composable calls `viewModel.initNewLog`. We should update `EditLogScreen` to take `rollId` and pass it.
            
            com.thatfluffyjello.analog.ui.editor.EditLogScreen(
                factory = viewModelFactory,
                logId = logId,
                imageUri = imageUri,
                rollId = rollId, // Need to update EditLogScreen signature
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
