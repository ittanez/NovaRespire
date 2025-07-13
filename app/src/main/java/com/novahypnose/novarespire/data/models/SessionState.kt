package com.novahypnose.novarespire.data.models

/**
 * États possibles d'une session de respiration
 */
sealed class SessionState {

    /**
     * État initial - session non démarrée
     */
    object Idle : SessionState()

    /**
     * Compte à rebours avant le début de la session
     */
    data class Countdown(val secondsLeft: Int) : SessionState()

    /**
     * Session active en cours
     */
    data class Active(
        val currentPhase: Phase,
        val phaseTimeRemaining: Int,
        val phaseProgress: Float,
        val sessionTimeRemaining: Int,
        val currentCycle: Int,
        val totalCycles: Int
    ) : SessionState() {

        /**
         * Calcule le progrès de la session (0.0 à 1.0)
         */
        fun getSessionProgress(totalSessionSeconds: Int): Float {
            val elapsed = totalSessionSeconds - sessionTimeRemaining
            return elapsed.toFloat() / totalSessionSeconds.toFloat()
        }
    }

    /**
     * Session en pause
     */
    data class Paused(
        val pausedAtPhase: Phase,
        val phaseTimeRemaining: Int,
        val sessionTimeRemaining: Int,
        val currentCycle: Int,
        val totalCycles: Int
    ) : SessionState()

    /**
     * Session terminée avec succès
     */
    object Completed : SessionState()

    /**
     * Session arrêtée par l'utilisateur
     */
    object Stopped : SessionState()
}