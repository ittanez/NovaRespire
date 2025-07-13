package com.novahypnose.novarespire.utils

/**
 * Toutes les chaînes de caractères de l'application
 * Centralisées pour faciliter la traduction future
 */
object Strings {

    // ========================================
    // APPLICATION
    // ========================================
    const val app_name = "Nova Respire"
    const val app_subtitle = "Guide Anti-stress"

    // ========================================
    // ALAIN ZENATTI
    // ========================================
    const val created_by = "Créé par Alain Zenatti"
    const val hypnotherapist = "Hypnothérapeute | Nova Hypnose"
    const val learn_more = "En savoir plus >"
    const val website = "www.novahypnose.fr"
    const val alain_signature = "Alain ZENATTI\nHypnothérapeute"

    // ========================================
    // NAVIGATION & ACTIONS
    // ========================================
    const val back_button = "← Retour"
    const val start_button = "Commencer"
    const val pause_button = "Pause"
    const val resume_button = "Reprendre"
    const val stop_button = "Arrêter"
    const val new_session_button = "Nouvelle session"

    // ========================================
    // SESSION
    // ========================================
    const val session_duration = "Durée de la session"
    const val lets_go = "C'est parti !"
    const val ready = "PRÊT"
    const val pause_text = "PAUSE"

    // ========================================
    // PHASES DE RESPIRATION
    // ========================================
    const val inhale = "INSPIREZ"
    const val exhale = "EXPIREZ"
    const val hold = "RETENEZ"

    // ========================================
    // EXERCICES
    // ========================================
    const val exercise_478_name = "4-7-8"
    const val exercise_478_desc = "Respiration relaxante pour l'endormissement"

    const val exercise_coherence_name = "Cohérence cardiaque"
    const val exercise_coherence_desc = "5 sec inspiration, 5 sec expiration"

    const val exercise_square_name = "Respiration carrée"
    const val exercise_square_desc = "4-4-4-4 pour la concentration"

    const val exercise_calm_name = "Respiration apaisante"
    const val exercise_calm_desc = "Respiration libre et naturelle"

    // ========================================
    // ÉCRANS D'INFORMATION
    // ========================================
    const val how_it_works = "Comment ça fonctionne"
    const val the_steps = "Les étapes"
    const val choose_duration = "Choisir la durée"
    const val start_exercise = "Commencer cet exercice"

    // ========================================
    // À PROPOS
    // ========================================
    const val my_approach = "Mon approche, par Alain Zenatti"
    const val discover_coaching = "Découvrir mon accompagnement"

    const val alain_bio = "Je suis Alain Zenatti, Maître Hypnologue et créateur de cette application. " +
            "J'ai conçu Nova Respire comme une première porte d'entrée vers le calme intérieur.\n\n" +
            "Mon approche, l'hypnose ericksonienne, vous accompagne ensuite en douceur pour mobiliser " +
            "vos propres ressources et transformer durablement ce qui doit l'être."

    const val alain_cta = "Si ces exercices vous apaisent, imaginez ce que nous pouvons accomplir ensemble. " +
            "L'hypnose agit là où la volonté seule atteint ses limites."

    // ========================================
    // FÉLICITATIONS
    // ========================================
    const val congratulations = "✨ Félicitations !"
    const val session_complete = "Vous avez terminé votre session de respiration.\n\n" +
            "Prenez un moment pour ressentir les bienfaits de cet exercice."

    // ========================================
    // FORMATS & HELPERS
    // ========================================

    /**
     * Formate une durée en minutes
     */
    fun formatDuration(minutes: Int): String {
        return when (minutes) {
            1 -> "1 minute"
            else -> "$minutes minutes"
        }
    }

    /**
     * Formate un temps en MM:SS
     */
    fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * Retourne le texte d'instruction selon le type de phase
     */
    fun getPhaseInstruction(phaseType: com.novahypnose.novarespire.data.models.PhaseType): String {
        return when (phaseType) {
            com.novahypnose.novarespire.data.models.PhaseType.INHALE -> inhale
            com.novahypnose.novarespire.data.models.PhaseType.EXHALE -> exhale
            com.novahypnose.novarespire.data.models.PhaseType.HOLD -> hold
        }
    }
}