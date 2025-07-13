package com.novahypnose.novarespire.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// =============================================================================
// 🎨 SCHÉMAS DE COULEURS NOVA RESPIRE - PALETTE ALAIN ZENATTI
// =============================================================================

private val NovaLightColorScheme = lightColorScheme(
    // ✅ COULEURS PRINCIPALES CORRIGÉES
    primary = NovaBlueGreen,           // #68b0ab - Boutons principaux
    onPrimary = NovaWhite,             // Blanc sur primary

    // ✅ COULEURS SECONDAIRES
    secondary = NovaLightGreen,        // #f3f3f3 - Gris clair accent
    onSecondary = NovaDarkBlue,        // #2c3e50 - Texte sur secondary

    // ✅ COULEURS TERTIAIRES (CTA)
    tertiary = NovaOrange,             // #eab543 - Boutons CTA
    onTertiary = NovaWhite,            // Blanc sur tertiary

    // ✅ COULEURS DE SURFACE
    surface = NovaWhite,               // Blanc pour les cartes
    onSurface = NovaDarkBlue,          // #2c3e50 - Texte sur surface

    surfaceVariant = NovaLightGreen,   // #f3f3f3 - Surfaces alternatives
    onSurfaceVariant = NovaDarkBlue,   // #2c3e50 - Texte sur surfaceVariant

    // ✅ COULEURS DE CONTENEUR
    primaryContainer = NovaLightGreen, // #f3f3f3 - Conteneurs primary
    onPrimaryContainer = NovaDarkBlue, // #2c3e50 - Texte sur primaryContainer

    secondaryContainer = NovaBlueGreen, // #68b0ab - Conteneurs secondary
    onSecondaryContainer = NovaWhite,   // Blanc sur secondaryContainer

    tertiaryContainer = NovaOrange,     // #eab543 - Conteneurs tertiary
    onTertiaryContainer = NovaWhite,    // Blanc sur tertiaryContainer

    // ✅ COULEURS D'ARRIÈRE-PLAN
    background = NovaLightGreen,        // #f3f3f3 - Arrière-plan principal
    onBackground = NovaDarkBlue,        // #2c3e50 - Texte sur background

    // ✅ COULEURS SYSTÈME
    error = NovaError,                  // Rouge erreur
    onError = NovaWhite,               // Blanc sur error

    outline = NovaDarkBlue,            // #2c3e50 - Bordures
    outlineVariant = NovaBlueGreen     // #68b0ab - Bordures alternatives
)

private val NovaDarkColorScheme = darkColorScheme(
    // ✅ COULEURS PRINCIPALES MODE SOMBRE
    primary = NovaBlueGreen,           // #68b0ab - Conservé
    onPrimary = NovaWhite,

    // ✅ COULEURS SECONDAIRES MODE SOMBRE
    secondary = NovaBlueGreen,         // #68b0ab - Plus visible sur fond sombre
    onSecondary = NovaWhite,

    // ✅ COULEURS TERTIAIRES MODE SOMBRE
    tertiary = NovaOrange,             // #eab543 - Conservé pour les CTA
    onTertiary = NovaDarkBlue,

    // ✅ COULEURS DE SURFACE MODE SOMBRE
    surface = NovaDarkBlue,            // #2c3e50 - Fonds principaux sombres
    onSurface = NovaWhite,             // Blanc sur fond sombre

    surfaceVariant = NovaDarkGray,     // #3f3f3f - Surfaces alternatives
    onSurfaceVariant = NovaLightGreen, // #f3f3f3 - Texte clair sur surface sombre

    // ✅ COULEURS DE CONTENEUR MODE SOMBRE
    primaryContainer = NovaDarkBlue,   // #2c3e50 - Conteneurs sombres
    onPrimaryContainer = NovaLightGreen, // #f3f3f3

    secondaryContainer = NovaDarkGray, // #3f3f3f
    onSecondaryContainer = NovaLightGreen, // #f3f3f3

    tertiaryContainer = NovaOrange,    // #eab543
    onTertiaryContainer = NovaDarkBlue, // #2c3e50

    // ✅ COULEURS D'ARRIÈRE-PLAN MODE SOMBRE
    background = NovaDarkBlue,         // #2c3e50 - Arrière-plan principal sombre
    onBackground = NovaWhite,          // Blanc

    // ✅ COULEURS SYSTÈME MODE SOMBRE
    error = NovaError,
    onError = NovaWhite,

    outline = NovaLightGreen,          // #f3f3f3 - Bordures visibles
    outlineVariant = NovaDarkGray      // #3f3f3f
)

// =============================================================================
// 🎭 THÈME PRINCIPAL NOVA RESPIRE
// =============================================================================

@Composable
fun NovaRespireTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color désactivé pour utiliser nos couleurs personnalisées
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> NovaDarkColorScheme
        else -> NovaLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}