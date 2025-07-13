package com.novahypnose.novarespire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.data.models.Phase
import com.novahypnose.novarespire.data.models.SessionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BreathingSessionViewModel : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private var sessionJob: Job? = null
    private var currentExercise: Exercise? = null
    private var sessionDurationMinutes: Int = 0

    fun startSession(exercise: Exercise, durationMinutes: Int) {
        currentExercise = exercise
        sessionDurationMinutes = durationMinutes

        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            runSession(exercise, durationMinutes)
        }
    }

    private suspend fun runSession(exercise: Exercise, durationMinutes: Int) {
        // Countdown
        for (i in 3 downTo 1) {
            _sessionState.value = SessionState.Countdown(i)
            delay(1000)
        }
        _sessionState.value = SessionState.Countdown(0)
        delay(500)

        // Calculate session parameters
        val totalSessionSeconds = durationMinutes * 60
        val cycleDuration = exercise.phases.sumOf { it.durationSeconds }
        val totalCycles = totalSessionSeconds / cycleDuration

        var remainingTime = totalSessionSeconds
        var cycleCount = 0

        // Main session loop
        while (remainingTime > 0 && cycleCount < totalCycles) {
            for (phase in exercise.phases) {
                var phaseTime = phase.durationSeconds

                while (phaseTime > 0 && remainingTime > 0) {
                    val phaseProgress = 1f - (phaseTime.toFloat() / phase.durationSeconds.toFloat())

                    _sessionState.value = SessionState.Active(
                        currentPhase = phase,
                        phaseTimeRemaining = phaseTime,
                        phaseProgress = phaseProgress,
                        sessionTimeRemaining = remainingTime,
                        currentCycle = cycleCount + 1,
                        totalCycles = totalCycles.toInt()
                    )

                    delay(1000)
                    phaseTime--
                    remainingTime--
                }

                if (remainingTime <= 0) break
            }
            cycleCount++
        }

        _sessionState.value = SessionState.Completed
    }

    fun togglePause() {
        val currentState = _sessionState.value
        when (currentState) {
            is SessionState.Active -> {
                sessionJob?.cancel()
                _sessionState.value = SessionState.Paused(
                    pausedAtPhase = currentState.currentPhase,
                    phaseTimeRemaining = currentState.phaseTimeRemaining,
                    sessionTimeRemaining = currentState.sessionTimeRemaining,
                    currentCycle = currentState.currentCycle,
                    totalCycles = currentState.totalCycles
                )
            }
            is SessionState.Paused -> {
                // Resume from paused state
                sessionJob = viewModelScope.launch {
                    resumeSession(currentState)
                }
            }
            else -> {
                // Do nothing for other states
            }
        }
    }

    private suspend fun resumeSession(pausedState: SessionState.Paused) {
        val exercise = currentExercise ?: return

        // Resume from where we left off
        var remainingTime = pausedState.sessionTimeRemaining
        var phaseTime = pausedState.phaseTimeRemaining
        var currentPhaseIndex = exercise.phases.indexOf(pausedState.pausedAtPhase)
        var cycleCount = pausedState.currentCycle - 1

        // Continue the current phase
        val currentPhase = pausedState.pausedAtPhase
        while (phaseTime > 0 && remainingTime > 0) {
            val phaseProgress = 1f - (phaseTime.toFloat() / currentPhase.durationSeconds.toFloat())

            _sessionState.value = SessionState.Active(
                currentPhase = currentPhase,
                phaseTimeRemaining = phaseTime,
                phaseProgress = phaseProgress,
                sessionTimeRemaining = remainingTime,
                currentCycle = cycleCount + 1,
                totalCycles = pausedState.totalCycles
            )

            delay(1000)
            phaseTime--
            remainingTime--
        }

        // Continue with remaining phases and cycles
        val totalSessionSeconds = sessionDurationMinutes * 60
        val cycleDuration = exercise.phases.sumOf { it.durationSeconds }
        val totalCycles = totalSessionSeconds / cycleDuration

        while (remainingTime > 0 && cycleCount < totalCycles) {
            for (phaseIndex in (currentPhaseIndex + 1) until exercise.phases.size) {
                val phase = exercise.phases[phaseIndex]
                phaseTime = phase.durationSeconds

                while (phaseTime > 0 && remainingTime > 0) {
                    val phaseProgress = 1f - (phaseTime.toFloat() / phase.durationSeconds.toFloat())

                    _sessionState.value = SessionState.Active(
                        currentPhase = phase,
                        phaseTimeRemaining = phaseTime,
                        phaseProgress = phaseProgress,
                        sessionTimeRemaining = remainingTime,
                        currentCycle = cycleCount + 1,
                        totalCycles = totalCycles.toInt()
                    )

                    delay(1000)
                    phaseTime--
                    remainingTime--
                }

                if (remainingTime <= 0) break
            }

            cycleCount++
            currentPhaseIndex = -1 // Reset to start from first phase in next cycle
        }

        if (remainingTime <= 0) {
            _sessionState.value = SessionState.Completed
        }
    }

    fun stopSession() {
        sessionJob?.cancel()
        _sessionState.value = SessionState.Stopped
    }

    override fun onCleared() {
        super.onCleared()
        sessionJob?.cancel()
    }
}