package com.example.memory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showEndScreen by remember { mutableStateOf(false) }

            if (showEndScreen) {
                EndScreen(
                    onRestart = { showEndScreen = false }
                )
            } else {
                MemoryGame(
                    onGiveUp = { showEndScreen = true }
                )
            }
        }
    }
}

val buttonLabels = mutableListOf("0", "0", "1", "1", "2", "2", "3", "3", "4", "4", "5", "5")
val buttonLabels1 = buttonLabels.shuffled()

@Composable
fun MemoryGame(onGiveUp: () -> Unit) {
    var clickCount by remember { mutableIntStateOf(0) }

    var clickedButtons by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    var matchedButtons by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }

    val rows = 4
    val cols = 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(text = "Vinny's Memory Match", color = Color.Cyan, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Turns: $clickCount", color = Color.Cyan, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        for (rowIndex in 0 until rows) {
            val start = rowIndex * cols
            val end = start + cols
            val row = buttonLabels1.subList(start, end)

            ButtonRow(
                rowIndex = rowIndex,
                labels = row,
                clickedButtons = clickedButtons.toSet(),
                matchedButtons = matchedButtons,
                onButtonClick = { r, c ->
                    val key = r to c

                    if (matchedButtons.contains(key) || clickedButtons.contains(key)) return@ButtonRow

                    val newClicked = clickedButtons.toMutableList()

                    if (newClicked.size == 2) {
                        val (first, second) = newClicked
                        val firstLabel = buttonLabels1[first.first * cols + first.second]
                        val secondLabel = buttonLabels1[second.first * cols + second.second]

                        if (firstLabel == secondLabel) {
                            matchedButtons = matchedButtons + first + second
                        }

                        newClicked.clear()
                    }

                    newClicked.add(key)
                    clickedButtons = newClicked

                    if (newClicked.size == 2) {
                        val (first, second) = newClicked
                        val firstLabel = buttonLabels1[first.first * cols + first.second]
                        val secondLabel = buttonLabels1[second.first * cols + second.second]

                        if (firstLabel == secondLabel) {
                            matchedButtons = matchedButtons + first + second
                            clickedButtons = emptyList() // clear immediately if matched
                        }
                    }

                    clickCount++
                }

            )

            Spacer(modifier = Modifier.height(45.dp))
        }

        val allMatched = matchedButtons.size == buttonLabels1.size

        DifferentCentered(
            label = if (allMatched) "You Win!" else "Give Up?",
            width = 400.dp,
            isClicked = false,
            onClick = {
                if (!allMatched) onGiveUp()
            }
        )

    }
}

@Composable
fun ButtonRow(
    rowIndex: Int,
    labels: List<String>,
    clickedButtons: Set<Pair<Int, Int>>,
    matchedButtons: Set<Pair<Int, Int>>,
    onButtonClick: (Int, Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        labels.forEachIndexed { columnIndex, label ->
            val key = rowIndex to columnIndex
            val isClicked = clickedButtons.contains(key) || matchedButtons.contains(key)
            val isMatched = matchedButtons.contains(key)

            CenteredButton(
                label = label,
                isClicked = isClicked,
                isMatched = isMatched,
                onClick = { onButtonClick(rowIndex, columnIndex) }
            )

            if (columnIndex != labels.lastIndex) {
                Spacer(modifier = Modifier.width(45.dp))
            }
        }
    }
}

@Composable
fun CenteredButton(
    label: String,
    isClicked: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit,
    width: Dp = 100.dp,
    height: Dp = 100.dp
) {
    val backgroundColor = when {
        isMatched -> Color.Yellow
        isClicked -> Color.Gray
        else -> Color.DarkGray
    }

    val contentColor = if (isClicked || isMatched) Color.Black else Color.DarkGray

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(1.dp)
    ) {
        Text(
            text = if (isClicked || isMatched) label else "",
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DifferentCentered(
    label: String,
    isClicked: Boolean,
    onClick: () -> Unit,
    width: Dp = 100.dp,
    height: Dp = 100.dp
) {
    val backgroundColor = if (isClicked) Color.Gray else Color.DarkGray
    val contentColor = if (isClicked) Color.White else Color.DarkGray

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(1.dp)
    ) {
        Text(
            text = label,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun EndScreen(onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Game Over!",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(300.dp)
                .height(100.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Restart",
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
