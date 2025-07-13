package com.novahypnose.novarespire.data.models

/**
 * États génériques pour l'interface utilisateur
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable, val message: String = exception.message ?: "Erreur inconnue") : UiState<Nothing>()
    
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

/**
 * États spécifiques pour les sessions de respiration
 */
sealed class SessionEvent {
    object Started : SessionEvent()
    object Paused : SessionEvent()
    object Resumed : SessionEvent()
    object Completed : SessionEvent()
    data class Error(val message: String, val cause: Throwable? = null) : SessionEvent()
    data class PhaseChanged(val phaseName: String) : SessionEvent()
}

/**
 * États pour la navigation
 */
sealed class NavigationEvent {
    object NavigateBack : NavigationEvent()
    data class NavigateToSession(val exerciseId: String, val duration: Int) : NavigationEvent()
    data class NavigateToAbout(val fromProfile: Boolean = false) : NavigationEvent()
    data class OpenExternalLink(val url: String) : NavigationEvent()
}