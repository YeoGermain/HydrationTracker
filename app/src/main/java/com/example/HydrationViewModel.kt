package com.example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HydrationUiState(
    val currentMl: Int = 0,
    val goalMl: Int = 2000,
    val lastAddedMl: Int? = null,
    val history: List<IntakeRecord> = emptyList()
) {
    val progress: Float
        get() = if (goalMl > 0) (currentMl.toFloat() / goalMl).coerceIn(0f, 1f) else 0f

    val percentage: Int
        get() = if (goalMl > 0) ((currentMl.toFloat() / goalMl) * 100).toInt() else 0

    val remainingMl: Int
        get() = (goalMl - currentMl).coerceAtLeast(0)

    val isGoalReached: Boolean
        get() = currentMl >= goalMl

    val glassesCount: Int
        get() = currentMl / 250
}

data class IntakeRecord(
    val id: Long,
    val amountMl: Int,
    val timeFormatted: String
)

class HydrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HydrationUiState())
    val uiState: StateFlow<HydrationUiState> = _uiState.asStateFlow()

    fun addWater(amountMl: Int = 250) {
        if (amountMl <= 0) return
        val now = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        val record = IntakeRecord(
            id = System.currentTimeMillis(),
            amountMl = amountMl,
            timeFormatted = now
        )
        _uiState.update { current ->
            val newAmount = current.currentMl + amountMl
            current.copy(
                currentMl = newAmount,
                lastAddedMl = amountMl,
                history = listOf(record) + current.history.take(19) // Keep last 20 entries
            )
        }
    }

    fun removeWater(amountMl: Int = 250) {
        _uiState.update { current ->
            val newAmount = (current.currentMl - amountMl).coerceAtLeast(0)
            current.copy(
                currentMl = newAmount,
                lastAddedMl = null
            )
        }
    }

    fun reset() {
        _uiState.update { current ->
            current.copy(
                currentMl = 0,
                lastAddedMl = null,
                history = emptyList()
            )
        }
    }
}
