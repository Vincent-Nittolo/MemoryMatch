package edu.gvsu.cis.memorymatching.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsScreen(viewModel: MainViewModel, onDismiss: () -> Unit) {
    val stats by viewModel.gameStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Game Statistics", fontSize = 24.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.sortStatsByMoves() }) {
                Text("Sort by Moves")
            }
            Button(onClick = { viewModel.sortStatsByDuration() }) {
                Text("Sort by Duration")
            }
            Button(
                onClick = onDismiss
            ) {
                Text("Exit")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(stats) { stat ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Total Tiles: ${stat.numCards}", fontSize = 16.sp)
                        Text("Moves: ${stat.numMoves}", fontSize = 16.sp)
                        Text("Duration: ${stat.duration} sec", fontSize = 16.sp)
                        Text("Completed: ${if (stat.completed) "Yes" else "No"}", fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onDismiss() }) {
            Text("Back")
        }
    }
}
