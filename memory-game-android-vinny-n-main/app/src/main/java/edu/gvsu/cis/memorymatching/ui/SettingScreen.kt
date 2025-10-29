package edu.gvsu.cis.memorymatching.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(viewModel: MainViewModel, onDismiss: () -> Unit) {
    var selectedCards by remember { mutableStateOf(viewModel.numCards) }
    var selectedColumns by remember { mutableStateOf(viewModel.numColumns) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", fontSize = 24.sp)

        Text("Number of cards (8-30, even only)")
        Slider(
            value = selectedCards.toFloat(),
            onValueChange = { newValue ->
                val evenValue = (newValue / 2).toInt() * 2
                selectedCards = evenValue.coerceIn(8, 30)
            },
            valueRange = 8f..30f,
            steps = (30 - 8) / 2 - 1
        )
        Text("Selected: $selectedCards")

        Text("Number of columns (3-5)")
        Slider(
            value = selectedColumns.toFloat(),
            onValueChange = { newValue ->
                selectedColumns = newValue.toInt().coerceIn(3, 5)
            },
            valueRange = 3f..5f,
            steps = 1
        )
        Text("Selected: $selectedColumns")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                viewModel.setNumCards(selectedCards)
                viewModel.setNumColumns(selectedColumns)
                viewModel.resetGame()
                onDismiss()
            }) { Text("Confirm") }

            Button(onClick = { onDismiss() }) { Text("Cancel") }
        }
    }
}
