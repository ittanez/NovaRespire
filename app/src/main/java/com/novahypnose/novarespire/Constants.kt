package com.novahypnose.novarespire


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novahypnose.novarespire.data.models.PhaseType

// ========================================
// CONSTANTES DE L'APPLICATION
// ========================================

object AppConstants {

    // ========================================
    // TIMING & DURÉES
    // ========================================

    object Timing {
        const val COUNTDOWN_DURATION_SECONDS = 3
        const val ANIMATION_DURATION_MS = 300
        const val COLOR_ANIMATION_DURATION_MS = 400
        const val PHASE_TRANSITION_DURATION_MS = 200
        const val PAUSE_CHECK_INTERVAL_MS = 100L
        const val SESSION_UPDATE_INTERVAL_MS = 1000L
    }

    // ========================================
    // DIMENSIONS & TAILLES
    // ========================================

    object Dimensions {
        // Breathing Guide
        val BREATHING_CIRCLE_BASE_SIZE = 160.dp
        val BREATHING_CIRCLE_MIN_SCALE = 0.3f
        val BREATHING_CIRCLE_MAX_SCALE = 2.5f
        val BREATHING_CIRCLE_SHADOW_ELEVATION = 8.dp

        // Contrôles
        val CONTROL_BUTTON_SIZE = 56.dp
        val CONTROL_BUTTON_SPACING = 16.dp

        // Cards et espacement
        val CARD_PADDING = 16.dp
        val SECTION_SPACING = 24.dp
        val SMALL_SPACING = 8.dp

        // Header
        val PROGRESS_BAR_HEIGHT = 6.dp
        val PROGRESS_BAR_WIDTH_FRACTION = 0.8f
    }

    // ========================================
    // TYPOGRAPHIE
    // ========================================

    object Typography {
        val TITLE_LARGE = 36.sp
        val TITLE_MEDIUM = 28.sp
        val TITLE_SMALL = 24.sp
        val BODY_LARGE = 18.sp
        val BODY_MEDIUM = 16.sp
        val BODY_SMALL = 14.sp
        val CAPTION = 12.sp

        // Spécifique au guide de respiration
        val INSTRUCTION_TEXT_SIZE = 28.sp
        val COUNTDOWN_TEXT_SIZE = 48.sp
        val SESSION_TIMER_SIZE = 24.sp
    }

    // ========================================
    // COULEURS THÉMATIQUES
    // ========================================

    object Colors {
        // Phases de respiration - Mode clair
        val INHALE_LIGHT = Color(0xFF4CAF50)
        val EXHALE_LIGHT = Color(0xFFFF6B6B)
        val HOLD_LIGHT = Color(0xFFFFC107)

        // Phases de respiration - Mode sombre
        val INHALE_DARK = Color(0xFF00E676)
        val EXHALE_DARK = Color(0xFFFF5722)
        val HOLD_DARK = Color(0xFFFFC107)

        // États
        val READY_STATE = Color(0xFF2196F3)
        val PAUSE_OVERLAY = Color(0x80000000) // Noir 50% alpha

        // Transparences
        const val SURFACE_ALPHA = 0.9f
        const val INSTRUCTION_ALPHA_ACTIVE = 1f
        const val INSTRUCTION_ALPHA_INACTIVE = 0.6f
        const val CIRCLE_ALPHA_HIGH = 0.9f
        const val CIRCLE_ALPHA_MEDIUM = 0.7f
        const val CIRCLE_ALPHA_LOW = 0.4f
        const val CIRCLE_ALPHA_MINIMAL = 0.1f
    }

    // ========================================
    // ANIMATIONS & EFFETS
    // ========================================

    object Animations {
        // Échelles pour les phases
        const val INHALE_START_SCALE = 0.5f
        const val INHALE_END_SCALE = 2.0f
        const val EXHALE_START_SCALE = 2.0f
        const val EXHALE_END_SCALE = 0.3f
        const val HOLD_PULSE_AMPLITUDE = 0.05f

        // Countdown
        const val COUNTDOWN_SMALL_SCALE = 1f
        const val COUNTDOWN_LARGE_SCALE = 1.2f
        const val COUNTDOWN_WARNING_THRESHOLD = 3

        // Background effects
        const val BUBBLE_COUNT = 6
        const val BUBBLE_BASE_SIZE = 25f
        const val BUBBLE_SIZE_VARIATION = 10f
        const val BUBBLE_ANIMATION_DURATION_MS = 15000L
    }

    // ========================================
    // CONFIGURATION EXERCICES
    // ========================================

    object Exercises {
        // Durées disponibles (en minutes)
        val AVAILABLE_DURATIONS = listOf(1, 2, 5, 10)
        val DEFAULT_DURATION = 2

        // IDs des exercices
        const val EXERCISE_478_ID = "478"
        const val EXERCISE_COHERENCE_ID = "coherence"
        const val EXERCISE_SQUARE_ID = "square"
        const val EXERCISE_CALM_ID = "calm"
    }

    // ========================================
    // LAYOUT & RESPONSIVE
    // ========================================

    object Layout {
        val SCREEN_PADDING_HORIZONTAL = 24.dp
        val SCREEN_PADDING_VERTICAL = 16.dp
        val CARD_CORNER_RADIUS = 12.dp
        val BUTTON_CORNER_RADIUS = 8.dp

        // Fractions d'écran
        const val BUTTON_WIDTH_FRACTION = 0.7f
        const val CARD_MAX_WIDTH_FRACTION = 0.9f
    }

    // ========================================
    // DEBUG & LOGGING
    // ========================================

    object Debug {
        const val TAG_SESSION = "BreathingSession"
        const val TAG_VIEWMODEL = "BreathingViewModel"
        const val TAG_ANIMATION = "BreathingAnimation"
        const val TAG_UI = "BreathingUI"

        const val ENABLE_PERFORMANCE_LOGS = true
        const val ENABLE_ANIMATION_LOGS = false
    }
}

// ========================================
// EXTENSIONS UTILITAIRES
// ========================================

/**
 * Extensions pour faciliter l'utilisation des constantes
 */

// Couleurs en fonction du mode
fun getPhaseColor(phaseType: PhaseType, isDarkMode: Boolean): Color {
    return if (isDarkMode) {
        when (phaseType) {
            PhaseType.INHALE -> AppConstants.Colors.INHALE_DARK
            PhaseType.EXHALE -> AppConstants.Colors.EXHALE_DARK
            PhaseType.HOLD -> AppConstants.Colors.HOLD_DARK
        }
    } else {
        when (phaseType) {
            PhaseType.INHALE -> AppConstants.Colors.INHALE_LIGHT
            PhaseType.EXHALE -> AppConstants.Colors.EXHALE_LIGHT
            PhaseType.HOLD -> AppConstants.Colors.HOLD_LIGHT
        }
    }
}

// Formatage du temps
fun formatSessionTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

// Validation des durées
fun isValidDuration(minutes: Int): Boolean {
    return minutes in AppConstants.Exercises.AVAILABLE_DURATIONS
}

// Log conditionnel pour debug
fun logDebug(tag: String, message: String) {
    if (AppConstants.Debug.ENABLE_PERFORMANCE_LOGS) {
        android.util.Log.d(tag, message)
    }
}