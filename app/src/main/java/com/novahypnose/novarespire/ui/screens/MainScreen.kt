package com.novahypnose.novarespire.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.compose.animation.core.*
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.utils.Strings
import com.novahypnose.novarespire.ui.theme.NovaColors  // ✅ IMPORT AJOUTÉ
import kotlin.math.*

/**
 * Écran principal avec NovaColors centralisées
 */
@Composable
fun MainScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onStartSession: (Exercise, Int) -> Unit,
    onLearnMore: () -> Unit,
    onShowExerciseInfo: (Exercise) -> Unit
) {
    var selectedExercise by remember { mutableStateOf(Exercise.getDefault()) }
    var selectedDuration by remember { mutableStateOf(2) }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Arrière-plan centralisé
        MainScreenBackground(isDarkMode = isDarkMode)

        // ✅ Effets de fond animés
        MainScreenBackgroundEffects()

        // ✅ Bouton mode sombre en position fixe
        DarkModeToggleButton(
            isDarkMode = isDarkMode,
            onToggleDarkMode = onToggleDarkMode,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = statusBarPadding + 16.dp)
                .padding(end = 16.dp)
        )

        // ✅ Contenu principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding + 16.dp)
                .padding(horizontal = 20.dp)
                .padding(bottom = navigationBarPadding + 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ En-tête de l'application
            AppHeaderCorrected()

            // ✅ Carte Alain Zenatti avec NovaColors
            AuthorCardSimplified(
                onLearnMore = onLearnMore,
                isDarkMode = isDarkMode
            )

            // ✅ Liste des exercices avec NovaColors
            ExerciseListCorrected(
                exercises = Exercise.getAll(),
                selectedExercise = selectedExercise,
                onExerciseSelected = { selectedExercise = it },
                onShowExerciseInfo = onShowExerciseInfo
            )

            // ✅ Sélection de durée avec NovaColors
            DurationSelectorCorrected(
                selectedDuration = selectedDuration,
                onDurationSelected = { selectedDuration = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Bouton "Commencer" avec NovaColors
            StartSessionButtonCorrected(
                onStartSession = {
                    Log.d("MainScreen", "🚀 DÉMARRAGE : ${selectedExercise.name} - ${selectedDuration}min")
                    onStartSession(selectedExercise, selectedDuration)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ========================================
// ARRIÈRE-PLAN AVEC NOVACOLORS ✨
// ========================================

@Composable
private fun MainScreenBackground(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isDarkMode) {
                    Brush.linearGradient(colors = NovaColors.darkGradient()) // ✅ CENTRALISÉ
                } else {
                    Brush.linearGradient(colors = NovaColors.primaryGradient()) // ✅ CENTRALISÉ
                }
            )
    )
}

@Composable
private fun MainScreenBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val bubbleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbles"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(6) { index ->
            val bubbleSize = 25f + (index % 2) * 10f
            val x = (index * 150f + sin(bubbleOffset * 1.5f + index) * 40f) % size.width
            val y = size.height - (bubbleOffset * (size.height + 150f)) + (index * 100f) % 250f

            if (y > -80f && y < size.height + 80f) {
                val alpha = 0.08f + sin(bubbleOffset * 2f + index) * 0.04f
                drawCircle(
                    color = NovaColors.White.copy(alpha = alpha), // ✅ CENTRALISÉ
                    radius = bubbleSize,
                    center = Offset(x, y)
                )
                drawCircle(
                    color = NovaColors.White.copy(alpha = alpha * 2), // ✅ CENTRALISÉ
                    radius = bubbleSize * 0.15f,
                    center = Offset(x - bubbleSize * 0.3f, y - bubbleSize * 0.3f)
                )
            }
        }
    }
}

// ========================================
// COMPOSANTS AVEC NOVACOLORS
// ========================================

@Composable
private fun DarkModeToggleButton(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onToggleDarkMode,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        modifier = modifier.size(48.dp)
    ) {
        Text(
            text = if (isDarkMode) "☀️" else "🌙",
            fontSize = 20.sp
        )
    }
}

@Composable
private fun AppHeaderCorrected() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        Text(
            text = Strings.app_name,
            fontSize = 38.sp,
            fontWeight = FontWeight.Light,
            color = NovaColors.White, // ✅ CENTRALISÉ
            textAlign = TextAlign.Center
        )

        Text(
            text = Strings.app_subtitle,
            fontSize = 20.sp,
            color = NovaColors.White.copy(alpha = 0.9f), // ✅ CENTRALISÉ
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )
    }
}

// ✅ CARTE AUTEUR AVEC NOVACOLORS
@Composable
private fun AuthorCardSimplified(
    onLearnMore: () -> Unit,
    isDarkMode: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) {
                NovaColors.Surface15 // Mode sombre : transparent
            } else {
                Color.White.copy(alpha = 0.95f) // Mode jour : blanc
            }
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Strings.created_by,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f), // TEXTE NOIR LISIBLE
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = Strings.hypnotherapist,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f), // TEXTE NOIR LISIBLE
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Bouton "En savoir plus" avec NovaColors
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onLearnMore,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NovaColors.CTA // ✅ CENTRALISÉ
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = "En savoir plus",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NovaColors.White, // ✅ CENTRALISÉ
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseListCorrected(
    exercises: List<Exercise>,
    selectedExercise: Exercise,
    onExerciseSelected: (Exercise) -> Unit,
    onShowExerciseInfo: (Exercise) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        exercises.forEach { exercise ->
            ExerciseCardCorrected(
                exercise = exercise,
                isSelected = selectedExercise.id == exercise.id,
                onExerciseSelected = { onExerciseSelected(exercise) },
                onShowExerciseInfo = { onShowExerciseInfo(exercise) },
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun ExerciseCardCorrected(
    exercise: Exercise,
    isSelected: Boolean,
    onExerciseSelected: () -> Unit,
    onShowExerciseInfo: () -> Unit,
    isDarkMode: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) {
                if (isSelected) NovaColors.Surface30 else NovaColors.Surface15
            } else {
                if (isSelected) 
                    Color.White.copy(alpha = 0.95f) 
                else 
                    Color.White.copy(alpha = 0.85f)
            }
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onExerciseSelected
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) NovaColors.White else Color.Black
                )
                Text(
                    text = exercise.description,
                    fontSize = 12.sp,
                    color = if (isDarkMode) NovaColors.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // ✅ Bouton info avec bordure NovaColors
            Card(
                onClick = onShowExerciseInfo,
                modifier = Modifier.size(36.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NovaColors.Transparent // ✅ CENTRALISÉ
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = NovaColors.CTA // ✅ CENTRALISÉ
                ),
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "i",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFeab543) // ORANGE ASSORTI
                    )
                }
            }
        }
    }
}

@Composable
private fun DurationSelectorCorrected(
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = Strings.session_duration,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NovaColors.White, // ✅ CENTRALISÉ
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(1, 2, 5).forEach { duration ->
                DurationButtonCorrected(
                    duration = duration,
                    isSelected = selectedDuration == duration,
                    onSelected = { onDurationSelected(duration) }
                )
            }
        }
    }
}

@Composable
private fun DurationButtonCorrected(
    duration: Int,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        onClick = onSelected,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                NovaColors.CTA // ✅ CENTRALISÉ
            else
                NovaColors.Surface20 // ✅ CENTRALISÉ
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Text(
            text = "$duration min",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = NovaColors.White, // ✅ CENTRALISÉ
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun StartSessionButtonCorrected(onStartSession: () -> Unit) {
    Card(
        onClick = onStartSession,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(56.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary // ✅ DÉJÀ CENTRALISÉ
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = Strings.start_button,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary, // ✅ DÉJÀ CENTRALISÉ
                fontWeight = FontWeight.Bold
            )
        }
    }
}