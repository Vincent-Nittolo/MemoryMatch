package edu.gvsu.cis.memorymatching.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class GameStat(
    val numCards: Int,
    val numMoves: Int,
    val duration: Int,
    val completed: Boolean
)
class MainViewModel : ViewModel() {

    val isGameComplete: Boolean
        get() = _cardStates.all { it == null }
    private var gameStartTime: Long = 0L
    private val _numCards = mutableStateOf(20)
    val numCards get() = _numCards.value

    private val _numColumns = mutableStateOf(4)
    val numColumns get() = _numColumns.value

    private var _cards = List(_numCards.value) { it / 2 }.shuffled()
    val cards: List<Int> get() = _cards
    private val _cardStates = mutableStateListOf<Boolean?>().apply {
        addAll(List(_numCards.value) { false })
    }
    val cardStates: List<Boolean?> get() = _cardStates

    private val _moves = mutableStateOf(0)
    val moves get() = _moves.value

    private var tapCount = 0
    private var lastRevealedIndex: Int? = null

    private var secondLastRevealedIndex: Int? = null
    private var isProcessing = false

    private var _gameStats = MutableStateFlow<List<GameStat>>(emptyList())
    val gameStats = _gameStats.asStateFlow()

    fun addGameStat(stat: GameStat) {
        _gameStats.update { it + stat }
    }

    fun sortStatsByMoves() {
        _gameStats.update { it.sortedBy { stat -> stat.numMoves } }
    }

    fun sortStatsByDuration() {
        _gameStats.update { it.sortedBy { stat -> stat.duration } }
    }

    fun resetGame() {
        _moves.value = 0
        tapCount = 0
        lastRevealedIndex = null
        secondLastRevealedIndex = null
        _cards = List(_numCards.value) { it / 2 }.shuffled()
        _cardStates.clear()
        _cardStates.addAll(List(_numCards.value) { false })
    }

    fun setNumCards(cards: Int) {
        if (cards in 8..30 && cards % 2 == 0) {
            _numCards.value = cards
        }
    }

    fun setNumColumns(columns: Int) {
        if (columns in 3..5) {
            _numColumns.value = columns
        }
    }

    private fun hideUnmatchedCards() {
        for (i in _cardStates.indices) {
            if (_cardStates[i] == true) {
                _cardStates[i] = false
            }
        }
    }

    fun onCardTapped(index: Int) {
        if (_cardStates[index] != false || isProcessing) return

        tapCount++

        viewModelScope.launch {
            if (tapCount % 2 == 1) {
                isProcessing = true
                hideUnmatchedCards()
                _cardStates[index] = true
                secondLastRevealedIndex = lastRevealedIndex
                lastRevealedIndex = index
                isProcessing = false
            } else {
                _cardStates[index] = true
                _moves.value += 1

                lastRevealedIndex?.let { firstIndex ->
                    if (_cards[firstIndex] == _cards[index]) {
                        // Match found → remove both
                        _cardStates[firstIndex] = null
                        _cardStates[index] = null
                    }
                }

                secondLastRevealedIndex = lastRevealedIndex
                lastRevealedIndex = index
            }
        }
    }
    fun startGame() {
        gameStartTime = System.currentTimeMillis()
        resetGameInternal()
    }

    private fun resetGameInternal() {
        _moves.value = 0
        tapCount = 0
        lastRevealedIndex = null
        _cards = List(_numCards.value) { it / 2 }.shuffled()
        _cardStates.clear()
        _cardStates.addAll(List(_numCards.value) { false })
    }
    fun getElapsedSeconds(): Int {
        val now = System.currentTimeMillis()
        return ((now - gameStartTime) / 1000).toInt()
    }
}