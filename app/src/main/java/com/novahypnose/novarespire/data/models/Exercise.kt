

package com.novahypnose.novarespire.data.models

// ========================================
// MODÈLES DE DONNÉES
// ========================================

/**
 * Représente un exercice de respiration complet
 */
data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val explanation: String,
    val phases: List<Phase>
) {
    companion object {
        /**
         * Retourne la liste de tous les exercices disponibles
         */
        fun getAll(): List<Exercise> = listOf(
            Exercise(
                id = "478",
                name = "4-7-8",
                description = "Respiration relaxante pour l'endormissement",
                explanation = "Technique développée par le Dr Andrew Weil. Cette méthode active le système nerveux parasympathique et favorise la relaxation profonde. Idéale avant le coucher pour calmer l'esprit et faciliter l'endormissement.",
                phases = listOf(
                    Phase("Inspirez par le nez", 4, PhaseType.INHALE),
                    Phase("Retenez votre souffle", 7, PhaseType.HOLD),
                    Phase("Expirez par la bouche", 8, PhaseType.EXHALE)
                )
            ),
            Exercise(
                id = "coherence",
                name = "Cohérence cardiaque",
                description = "5 sec inspiration, 5 sec expiration",
                explanation = "Technique qui synchronise le rythme cardiaque avec la respiration. Réduit le stress, améliore la concentration et régule les émotions. Pratique recommandée 3 fois par jour, 5 minutes par session.",
                phases = listOf(
                    Phase("Inspirez lentement", 5, PhaseType.INHALE),
                    Phase("Expirez doucement", 5, PhaseType.EXHALE)
                )
            ),
            Exercise(
                id = "square",
                name = "Respiration carrée",
                description = "4-4-4-4 pour la concentration",
                explanation = "Technique utilisée par les forces spéciales pour gérer le stress. Les 4 temps égaux créent un rythme régulier qui calme l'esprit et améliore la concentration. Très efficace avant des situations stressantes.",
                phases = listOf(
                    Phase("Inspirez", 4, PhaseType.INHALE),
                    Phase("Retenez", 4, PhaseType.HOLD),
                    Phase("Expirez", 4, PhaseType.EXHALE),
                    Phase("Retenez", 4, PhaseType.HOLD)
                )
            ),
            Exercise(
                id = "calm",
                name = "Respiration apaisante",
                description = "Respiration libre et naturelle",
                explanation = "Respiration douce qui privilégie une expiration plus longue. Cette technique active la réponse de relaxation et diminue l'anxiété naturellement. Parfaite pour les moments de stress quotidien.",
                phases = listOf(
                    Phase("Inspirez naturellement", 4, PhaseType.INHALE),
                    Phase("Expirez lentement", 6, PhaseType.EXHALE)
                )
            )
        )

        /**
         * Trouve un exercice par son ID
         */
        fun findById(id: String): Exercise? = getAll().find { it.id == id }

        /**
         * Retourne l'exercice par défaut
         */
        fun getDefault(): Exercise = getAll().first()
    }
}

/**
 * Représente une phase d'un exercice de respiration
 */
data class Phase(
    val instruction: String,
    val durationSeconds: Int,
    val type: PhaseType
)

/**
 * Types de phases de respiration
 */
enum class PhaseType {
    INHALE,  // Inspiration
    EXHALE,  // Expiration
    HOLD     // Rétention
}