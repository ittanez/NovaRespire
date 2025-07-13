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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.novahypnose.novarespire.data.models.Phase
import com.novahypnose.novarespire.data.models.PhaseType
import kotlin.math.cos
import kotlin.math.sin
import com.novahypnose.novarespire.ui.theme.BreathingInhale
import com.novahypnose.novarespire.ui.theme.BreathingExhale
import com.novahypnose.novarespire.ui.theme.BreathingHold

/**
 * Forme hexagonale pour la phase EXHALE
 */
class HexagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val radius = minOf(size.width, size.height) / 2f
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        
        // Créer un hexagone
        for (i in 0..5) {
            val angle = (i * 60.0 - 90.0) * Math.PI / 180.0
            val x = centerX + (radius * cos(angle)).toFloat()
            val y = centerY + (radius * sin(angle)).toFloat()
            
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}

@Composable
fun BreathingGuide(
    currentPhase: Phase,
    isActive: Boolean,
    phaseProgress: Float,
    phaseTimeRemaining: Int,
    isDarkMode: Boolean,
    exerciseName: String = "",
    techniqueName: String = "Technique de Respiration"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathingTransition")

    val targetScale = when (currentPhase.type) {
        PhaseType.INHALE -> 2.0f
        PhaseType.EXHALE -> 0.5f
        PhaseType.HOLD -> 2.0f
    }

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = if (currentPhase.type == PhaseType.EXHALE) 2.0f else 0.5f,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (currentPhase.durationSeconds * 1000).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    // ✅ Couleurs définies dans Color.kt
    val phaseColor = when (currentPhase.type) {
        PhaseType.INHALE -> BreathingInhale   // #a9e4d7 - Vert clair pour INSPIREZ
        PhaseType.EXHALE -> BreathingExhale   // #2c3e50 - Bleu-gris pour EXPIREZ
        PhaseType.HOLD -> BreathingHold       // #eab543 - Jaune-orange pour RETENEZ
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // ✅ Carte technique avec vos couleurs
        if (techniqueName.isNotEmpty() && isActive) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                ),
                shape = CircleShape
            ) {
                Text(
                    text = techniqueName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // ✅ Carte exercice avec vos couleurs
        if (exerciseName.isNotEmpty() && isActive) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = CircleShape
            ) {
                Text(
                    text = exerciseName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // ✅ Forme de respiration avec vos couleurs (hexagone pour EXHALE)
        SimpleBreathingCircle(
            scale = if (currentPhase.type == PhaseType.HOLD) 2.0f else animatedScale,
            color = phaseColor,
            isActive = isActive,
            phaseType = currentPhase.type
        )

        // ✅ Instructions avec vos couleurs
        SimpleBreathingInstructions(
            phase = currentPhase,
            timeRemaining = phaseTimeRemaining,
            isActive = isActive
        )
    }
}

@Composable
private fun SimpleBreathingCircle(
    scale: Float,
    color: Color,
    isActive: Boolean,
    phaseType: PhaseType = PhaseType.INHALE
) {
    val baseSize = 180.dp
    val finalScale = scale.coerceIn(0.3f, 2.5f)
    
    // Forme selon la phase
    val shape = when (phaseType) {
        PhaseType.EXHALE -> HexagonShape() // Hexagone pour expirez
        else -> CircleShape // Cercle pour inspirez et reteez
    }

    Box(
        modifier = Modifier
            .size(baseSize * finalScale)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 1.0f),
                        color.copy(alpha = 0.8f),
                        color.copy(alpha = 0.6f),
                        color.copy(alpha = 0.4f),
                        color.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                ),
                shape = shape
            )
            .shadow(
                elevation = if (isActive) 12.dp else 4.dp,
                shape = shape,
                ambientColor = color.copy(alpha = 0.4f),
                spotColor = color.copy(alpha = 0.6f)
            )
    )
}

@Composable
private fun SimpleBreathingInstructions(
    phase: Phase,
    timeRemaining: Int,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val instructionText = when (phase.type) {
            PhaseType.INHALE -> "Inspirez"
            PhaseType.EXHALE -> "Expirez"
            PhaseType.HOLD -> "Retenez"
        }

        Text(
            text = if (isActive) instructionText else "Pause",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.7f),
                    offset = Offset(2f, 2f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (timeRemaining > 0 && isActive) {
            Text(
                text = timeRemaining.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(3f, 3f),
                        blurRadius = 8f
                    )
                )
            )
        }
    }
}