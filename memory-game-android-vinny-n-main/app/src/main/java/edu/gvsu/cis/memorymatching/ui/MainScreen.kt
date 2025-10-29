package edu.gvsu.cis.memorymatching.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.gvsu.cis.memorymatching.R

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.startGame()
    }

    val cardStates = viewModel.cardStates
    val cards = viewModel.cards
    val moves = viewModel.moves
    val gameComplete = viewModel.isGameComplete

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Moves: $moves", fontSize = 20.sp, modifier = Modifier.padding(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(viewModel.numColumns),
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(cards) { idx, value ->
                MemoryCell(
                    state = cardStates[idx],
                    value = value,
                    onClick = { viewModel.onCardTapped(idx) }
                )
            }
        }
        if (gameComplete) {
            Text(
                "You found all Matches!",
                fontSize = 20.sp,
                color = Color.Green,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(onClick = {
                // Record statistics
                viewModel.addGameStat(
                    GameStat(
                        numCards = viewModel.numCards,
                        numMoves = viewModel.moves,
                        duration = viewModel.getElapsedSeconds(),
                        completed = viewModel.isGameComplete
                    )
                )

                viewModel.resetGame()
            }) {
                Text("Restart")
            }

            Button(onClick = { onNavigateToSettings() }) { Text("Settings") }
            Button(onClick = { onNavigateToStats() }) { Text("Statistics") }
        }
    }
}

@Composable
fun MemoryCell(state: Boolean?, value: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(2.dp, Color.Blue)
            .clickable(enabled = state == false) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }
            true -> {
                val imageRes = when (value) {
                    0 -> R.drawable.image1
                    1 -> R.drawable.image2
                    2 -> R.drawable.image3
                    3 -> R.drawable.image4
                    4 -> R.drawable.image5
                    5 -> R.drawable.image6
                    6 -> R.drawable.image7
                    7 -> R.drawable.image8
                    8 -> R.drawable.image9
                    9 -> R.drawable.image10
                    10 -> R.drawable.image11
                    11 -> R.drawable.image12
                    12 -> R.drawable.image13
                    13 -> R.drawable.image14
                    14 -> R.drawable.image15
                    15 -> R.drawable.image16
                    16 -> R.drawable.image17
                    17 -> R.drawable.image18
                    18 -> R.drawable.image19
                    19 -> R.drawable.image20
                    20 -> R.drawable.image21
                    21 -> R.drawable.image22
                    22 -> R.drawable.image23
                    23 -> R.drawable.image24
                    24 -> R.drawable.image25
                    25 -> R.drawable.image26
                    26 -> R.drawable.image27
                    27 -> R.drawable.image28
                    28 -> R.drawable.image29
                    29 -> R.drawable.image30
                    else -> R.drawable.image1
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Card $value",
                    modifier = Modifier.fillMaxSize()
                )
            }
            false -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                )
            }
        }
    }
}
