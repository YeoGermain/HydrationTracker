package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HydrationViewModelTest {

    private lateinit var viewModel: HydrationViewModel

    @Before
    fun setUp() {
        viewModel = HydrationViewModel()
    }

    @Test
    fun initialState_isZeroIntake() {
        val state = viewModel.uiState.value
        assertEquals(0, state.currentMl)
        assertEquals(2000, state.goalMl)
        assertEquals(0f, state.progress, 0.001f)
        assertEquals(2000, state.remainingMl)
        assertFalse(state.isGoalReached)
    }

    @Test
    fun add250ml_increasesIntakeAndCalculatesProgress() {
        viewModel.addWater(250)
        val state = viewModel.uiState.value
        assertEquals(250, state.currentMl)
        assertEquals(0.125f, state.progress, 0.001f)
        assertEquals(1750, state.remainingMl)
        assertEquals(1, state.glassesCount)
    }

    @Test
    fun reaching2000ml_marksGoalReached() {
        // Add 250ml 8 times = 2000ml
        repeat(8) {
            viewModel.addWater(250)
        }
        val state = viewModel.uiState.value
        assertEquals(2000, state.currentMl)
        assertEquals(1.0f, state.progress, 0.001f)
        assertEquals(0, state.remainingMl)
        assertTrue(state.isGoalReached)
    }

    @Test
    fun reset_resetsIntakeToZero() {
        viewModel.addWater(500)
        viewModel.reset()
        val state = viewModel.uiState.value
        assertEquals(0, state.currentMl)
        assertEquals(2000, state.remainingMl)
        assertFalse(state.isGoalReached)
    }
}
