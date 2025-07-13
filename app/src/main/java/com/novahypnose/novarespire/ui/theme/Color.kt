package com.novahypnose.novarespire.ui.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// 🎨 NOVA COLORS - TOUTES LES COULEURS CENTRALISÉES ICI
// =============================================================================

object NovaColors {
    // ✅ NOUVELLE PALETTE PRINCIPALE - 5 COULEURS
    val Primary = Color(0xFF4d699e)         // #4d699e - Bleu principal (Commencer)
    val PrimaryLight = Color(0xFF6382b1)    // #6382b1 - Bleu clair (gradients)
    val PrimaryDark = Color(0xFF36414c)     // #36414c - Gris-bleu foncé (mode sombre)
    val Accent = Color(0xFFcec0be)          // #cec0be - Beige clair (textes)
    val CTA = Color(0xFF874c4a)             // #874c4a - Rouge-brun (boutons CTA)

    // ✅ COULEURS RESPIRATION (inchangées)
    val Inhale = Color(0xFF43aa8b)          // Vert - INSPIREZ
    val Exhale = Color(0xFFd90429)          // Rouge - EXPIREZ
    val Hold = Color(0xFFfcba04)            // Jaune - RETENEZ

    // ✅ COULEURS SYSTÈME
    val White = Color.White
    val Black = Color.Black
    val Transparent = Color.Transparent
    val Error = Color(0xFFD32F2F)
    val Success = Color(0xFF4CAF50)

    // ✅ TRANSPARENCES COMMUNES
    val Surface10 = White.copy(alpha = 0.10f)
    val Surface15 = White.copy(alpha = 0.15f)
    val Surface20 = White.copy(alpha = 0.20f)
    val Surface30 = White.copy(alpha = 0.30f)
    val Surface90 = White.copy(alpha = 0.90f)

    // ✅ GRADIENTS PRÉDÉFINIS - NOUVELLE PALETTE
    fun primaryGradient() = listOf(Primary, PrimaryLight, Primary)        // Bleu principal
    fun darkGradient() = listOf(PrimaryDark, Color(0xFF2a3238), PrimaryDark) // Gris-bleu foncé
}

// =============================================================================
// 🔧 ALIAS POUR COMPATIBILITÉ (À SUPPRIMER PROGRESSIVEMENT)
// =============================================================================

// Couleurs principales (utilisées dans Theme.kt)
val NovaBlueGreen = NovaColors.Primary
val NovaLightGreen = NovaColors.Accent
val NovaOrange = NovaColors.CTA
val NovaDarkBlue = NovaColors.PrimaryDark
val NovaDarkGray = Color(0xFF3f3f3f)

// Couleurs respiration (utilisées dans OptimizedBreathingGuide.kt)
val BreathingInhale = NovaColors.Inhale
val BreathingExhale = NovaColors.Exhale
val BreathingHold = NovaColors.Hold

// Alias pour compatibilité
val InhaleColor = NovaColors.Inhale
val ExhaleColor = NovaColors.Exhale
val HoldColor = NovaColors.Hold

// Couleurs système
val NovaWhite = NovaColors.White
val NovaBlack = NovaColors.Black
val NovaError = NovaColors.Error
val NovaSuccess = NovaColors.Success
val NovaWarning = NovaColors.CTA

// =============================================================================
// 🔧 COULEURS MATERIAL DESIGN 3 - COMPATIBILITÉ
// =============================================================================

val Purple40 = NovaColors.Primary
val PurpleGrey40 = NovaColors.Accent
val Pink40 = NovaColors.CTA

val Purple80 = NovaColors.Primary
val PurpleGrey80 = NovaColors.PrimaryDark
val Pink80 = Color(0xFF3f3f3f)

// =============================================================================
// 🎨 GUIDE D'UTILISATION
// =============================================================================

/*
POUR CHANGER UNE COULEUR DANS TOUTE L'APP :

1. COULEUR PRINCIPALE (boutons, éléments principaux) :
   Modifiez NovaColors.Primary = Color(0xFFVOTRE_COULEUR)

2. COULEUR CTA (boutons "En savoir plus", sélections) :
   Modifiez NovaColors.CTA = Color(0xFFVOTRE_COULEUR)

3. COULEURS RESPIRATION :
   Modifiez NovaColors.Inhale/Exhale/Hold

DANS VOS COMPOSANTS, UTILISEZ :
- NovaColors.Primary au lieu de Color(0xFF...)
- NovaColors.CTA au lieu de Color(0xFFeab543)
- NovaColors.Surface15 au lieu de Color.White.copy(alpha = 0.15f)

EXEMPLE :
❌ containerColor = Color(0xFF4c8c72)
✅ containerColor = NovaColors.Primary

❌ Brush.linearGradient(colors = listOf(Color(0xFF...), Color(0xFF...)))
✅ Brush.linearGradient(colors = NovaColors.primaryGradient())
*/