package edu.gvsu.cis.memorymatching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import edu.gvsu.cis.memorymatching.ui.*
import edu.gvsu.cis.memorymatching.ui.theme.MemoryMatchingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryMatchingTheme {
                MemoryMatchingApp()
            }
        }
    }
}

@Composable
fun MemoryMatchingApp() {
    val viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var currentScreen by remember { mutableStateOf(1) }

    when (currentScreen) {
        1 -> MainScreen(
            viewModel = viewModel,
            onNavigateToSettings = { currentScreen = 2 },
            onNavigateToStats = { currentScreen = 3 }
        )
        2 -> SettingsScreen(
            viewModel = viewModel,
            onDismiss = { currentScreen = 1 }
        )
        3 -> StatsScreen(
            viewModel = viewModel,
            onDismiss = { currentScreen = 1 }
        )
    }
}
