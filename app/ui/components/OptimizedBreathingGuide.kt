
package com.novahypnose.novarespire.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novahypnose.novarespire.data.models.Phase
import com.novahypnose.novarespire.data.models.PhaseType
import com.novahypnose.novarespire.ui.theme.NovaColors  // ✅ IMPORT NOVACOLORS
import kotlin.math.*

@Composable
fun OptimizedBreathingGuide(
    currentPhase: Phase,
    isActive: Boolean,
    phaseProgress: Float,
    phaseTimeRemaining: Int,
    isDarkMode: Boolean,
    exerciseName: String = "",
    techniqueName: String = "Technique de Respiration"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathingTransition")

    // ✅ ANIMATION PLUS DOUCE POUR DES CERCLES PARFAITS
    val targetScale = when (currentPhase.type) {
        PhaseType.INHALE -> 1.6f  // ✅ RÉDUIT pour éviter débordement
        PhaseType.EXHALE -> 0.7f  // ✅ AUGMENTÉ pour visibilité minimale
        PhaseType.HOLD -> 1.4f    // ✅ RÉDUIT pour éviter débordement
    }

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = if (currentPhase.type == PhaseType.EXHALE) 1.6f else 0.7f,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (currentPhase.durationSeconds * 1000).toInt(),
                easing = EaseInOutCubic  // ✅ ANIMATION TRÈS DOUCE
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    // ✅ COULEURS RESPIRATION (inchangées - parfaites)
    val phaseColor = when (currentPhase.type) {
        PhaseType.INHALE -> NovaColors.Inhale   // #43aa8b - Vert inspirez
        PhaseType.EXHALE -> NovaColors.Exhale   // #d90429 - Rouge expirez
        PhaseType.HOLD -> NovaColors.Hold       // #fcba04 - Jaune retenez
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // ✅ Carte technique - TEXTE PLUS PETIT
        if (techniqueName.isNotEmpty() && isActive) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NovaColors.Surface15 // ✅ NOVA COLORS
                ),
                shape = CircleShape
            ) {
                Text(
                    text = techniqueName,
                    fontSize = 12.sp, // ✅ RÉDUIT
                    fontWeight = FontWeight.Medium,
                    color = NovaColors.White.copy(alpha = 0.8f), // ✅ NOVA COLORS
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // ✅ Carte exercice - TEXTE PLUS PETIT
        if (exerciseName.isNotEmpty() && isActive) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NovaColors.Surface15 // ✅ NOVA COLORS
                ),
                shape = CircleShape
            ) {
                Text(
                    text = exerciseName,
                    fontSize = 12.sp, // ✅ RÉDUIT
                    fontWeight = FontWeight.Medium,
                    color = NovaColors.White.copy(alpha = 0.8f), // ✅ NOVA COLORS
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // ✅ CERCLE DE RESPIRATION PARFAIT
        PerfectBreathingCircle(
            scale = if (currentPhase.type == PhaseType.HOLD) 1.4f else animatedScale,
            color = phaseColor,
            isActive = isActive
        )

        // ✅ INSTRUCTIONS OPTIMISÉES
        PerfectBreathingInstructions(
            phase = currentPhase,
            timeRemaining = phaseTimeRemaining,
            isActive = isActive
        )
    }
}

@Composable
private fun PerfectBreathingCircle(
    scale: Float,
    color: Color,
    isActive: Boolean
) {
    // ✅ TAILLE OPTIMALE POUR DES CERCLES PARFAITS
    val baseSize = 120.dp
    val finalScale = scale.coerceIn(0.5f, 1.8f)

    Box(
        modifier = Modifier
            .size(baseSize * finalScale)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.9f),   // ✅ CENTRE OPAQUE
                        color.copy(alpha = 0.7f),
                        color.copy(alpha = 0.5f),
                        color.copy(alpha = 0.3f),
                        color.copy(alpha = 0.1f),
                        Color.Transparent           // ✅ BORDURE TRANSPARENTE
                    ),
                    radius = (baseSize.value * finalScale) * 0.8f // ✅ RAYON PARFAIT
                ),
                shape = CircleShape  // ✅ FORME CIRCULAIRE GARANTIE
            )
            .shadow(
                elevation = if (isActive) 20.dp else 8.dp, // ✅ OMBRE PLUS MARQUÉE
                shape = CircleShape,  // ✅ OMBRE CIRCULAIRE
                ambientColor = color.copy(alpha = 0.4f),
                spotColor = color.copy(alpha = 0.6f)
            )
    )
}

@Composable
private fun PerfectBreathingInstructions(
    phase: Phase,
    timeRemaining: Int,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp) // ✅ PADDING POUR ÉVITER CHEVAUCHEMENT
    ) {
        val instructionText = when (phase.type) {
            PhaseType.INHALE -> "INSPIREZ"   // ✅ MAJUSCULES POUR IMPACT
            PhaseType.EXHALE -> "EXPIREZ"
            PhaseType.HOLD -> "RETENEZ"
        }

        // ✅ TEXTE D'INSTRUCTION OPTIMISÉ
        Text(
            text = if (isActive) instructionText else "PAUSE",
            fontSize = 18.sp, // ✅ TAILLE PARFAITE
            fontWeight = FontWeight.Bold,
            color = NovaColors.White, // ✅ NOVA COLORS
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = NovaColors.Black.copy(alpha = 0.8f), // ✅ NOVA COLORS
                    offset = Offset(2f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ✅ COMPTEUR OPTIMISÉ
        if (timeRemaining > 0 && isActive) {
            Text(
                text = timeRemaining.toString(),
                fontSize = 32.sp, // ✅ TAILLE PARFAITE
                fontWeight = FontWeight.Black,
                color = NovaColors.White, // ✅ NOVA COLORS
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = NovaColors.Black.copy(alpha = 0.9f), // ✅ NOVA COLORS
                        offset = Offset(2f, 2f),
                        blurRadius = 8f
                    )
                )
            )
        }
    }
}