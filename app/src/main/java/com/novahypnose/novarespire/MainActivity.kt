// ========================================
// FICHIER MAINACTIVITY.KT - VERSION COMPLÈTE FINALE
// ========================================

package com.novahypnose.novarespire

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.media.MediaPlayer
import android.media.AudioManager
import android.media.AudioAttributes
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.draw.shadow
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.data.models.Phase
import com.novahypnose.novarespire.data.models.PhaseType
import com.novahypnose.novarespire.data.models.SessionState
import com.novahypnose.novarespire.utils.Strings
import com.novahypnose.novarespire.ui.screens.MainScreen
import com.novahypnose.novarespire.ui.screens.SessionScreen
import com.novahypnose.novarespire.ui.screens.ProfileScreen
import com.novahypnose.novarespire.ui.components.OptimizedBreathingGuide
import com.novahypnose.novarespire.ui.theme.NovaRespireTheme
import com.novahypnose.novarespire.ui.theme.BreathingInhale
import com.novahypnose.novarespire.ui.theme.BreathingExhale
import com.novahypnose.novarespire.ui.theme.BreathingHold
import kotlinx.coroutines.delay
import kotlin.math.*

// =============================================================================
// ACTIVITÉ PRINCIPALE
// =============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            NovaRespireTheme(
                darkTheme = isDarkMode,
                dynamicColor = false
            ) {
                AppMain(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

// =============================================================================
// FONCTION PRINCIPALE DE L'APP
// =============================================================================

@Composable
fun AppMain(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedExercise by remember { mutableStateOf(getExerciseList()[0]) }
    var selectedDuration by remember { mutableStateOf(5) }
    var soundEnabled by remember { mutableStateOf(true) }
    var backgroundMusicEnabled by remember { mutableStateOf(false) }

    when (currentScreen) {
        "home" -> MainScreen(
            isDarkMode = isDarkMode,
            onToggleDarkMode = onToggleDarkMode,
            onStartSession = { exercise, duration ->
                selectedExercise = exercise
                selectedDuration = duration
                currentScreen = "session"
            },
            onLearnMore = { currentScreen = "profile" },
            onShowExerciseInfo = { exercise ->
                selectedExercise = exercise
                currentScreen = "exercise_info"
            }
        )
        "session" -> BreathingSession(
            exercise = selectedExercise,
            duration = selectedDuration,
            isDarkMode = isDarkMode,
            soundEnabled = soundEnabled,
            backgroundMusicEnabled = backgroundMusicEnabled,
            onToggleSound = { soundEnabled = !soundEnabled },
            onToggleBackgroundMusic = { backgroundMusicEnabled = !backgroundMusicEnabled },
            onComplete = { currentScreen = "completion" },
            onStop = { currentScreen = "home" }
        )
        "completion" -> FinishScreen(
            isDarkMode = isDarkMode,
            onNewSession = { currentScreen = "home" }
        )
        "exercise_info" -> ExerciseInfoScreen(
            exercise = selectedExercise,
            isDarkMode = isDarkMode,
            onBack = { currentScreen = "home" },
            onStartSession = { duration ->
                selectedDuration = duration
                currentScreen = "session"
            }
        )
        "profile" -> ProfileScreen(
            isDarkMode = isDarkMode,
            onBack = { currentScreen = "home" }
        )
    }
}

// =============================================================================
// DONNÉES DES EXERCICES
// =============================================================================

fun getExerciseList(): List<Exercise> = Exercise.getAll()

// =============================================================================
// COMPOSANTS IMAGE SIMPLIFIÉS
// =============================================================================

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isDarkMode) {
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            )
    )
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AZ",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

// =============================================================================
// EFFETS DE FOND ANIMÉS
// =============================================================================

@Composable
fun BackgroundEffects() {
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
                    color = Color.White.copy(alpha = alpha),
                    radius = bubbleSize,
                    center = Offset(x, y)
                )
                drawCircle(
                    color = Color.White.copy(alpha = alpha * 2),
                    radius = bubbleSize * 0.15f,
                    center = Offset(x - bubbleSize * 0.3f, y - bubbleSize * 0.3f)
                )
            }
        }
    }
}

// =============================================================================
// SESSION DE RESPIRATION
// =============================================================================

@Composable
fun BreathingSession(
    exercise: Exercise,
    duration: Int,
    isDarkMode: Boolean,
    soundEnabled: Boolean,
    backgroundMusicEnabled: Boolean,
    onToggleSound: () -> Unit,
    onToggleBackgroundMusic: () -> Unit,
    onComplete: () -> Unit,
    onStop: () -> Unit
) {
    SessionScreen(
        exercise = exercise,
        duration = duration,
        isDarkMode = isDarkMode,
        soundEnabled = soundEnabled,
        backgroundMusicEnabled = backgroundMusicEnabled,
        onToggleSound = onToggleSound,
        onToggleBackgroundMusic = onToggleBackgroundMusic,
        onComplete = onComplete,
        onStop = onStop
    )
}

// =============================================================================
// ÉCRAN D'INFORMATIONS SUR L'EXERCICE - CORRIGÉ
// =============================================================================

@Composable
fun ExerciseInfoScreen(
    exercise: Exercise,
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onStartSession: (Int) -> Unit
) {
    var selectedDuration by remember { mutableStateOf(2) }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(isDarkMode = isDarkMode)
        BackgroundEffects()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(Strings.back_button, color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = exercise.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Column(modifier = Modifier.padding(32.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = Strings.how_it_works,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = exercise.explanation,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = Strings.the_steps,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        exercise.phases.forEachIndexed { index, phase ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            // ✅ CORRECTION - utiliser when avec ->
                                            when (phase.type) {
                                                PhaseType.INHALE -> BreathingInhale
                                                PhaseType.EXHALE -> BreathingExhale
                                                PhaseType.HOLD -> BreathingHold
                                            },
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = "${phase.instruction} (${phase.durationSeconds}s)",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = Strings.choose_duration,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    listOf(1, 2, 5).forEach { duration ->
                        Button(
                            onClick = { selectedDuration = duration },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedDuration == duration)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                Strings.formatDuration(duration),
                                color = if (selectedDuration == duration)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Button(
                    onClick = { onStartSession(selectedDuration) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = Strings.start_exercise,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

// =============================================================================
// ÉCRAN DE FIN DE SESSION - CORRIGÉ
// =============================================================================

@Composable
fun FinishScreen(
    isDarkMode: Boolean,
    onNewSession: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    val celebration by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "celebration"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(isDarkMode = isDarkMode)

        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(20) { index ->
                val angle = (celebration * 360f + index * 18f) * PI / 180f
                val radius = 80f + sin(celebration * 4f) * 40f
                val x = size.width / 2 + cos(angle) * radius
                val y = size.height / 2 + sin(angle) * radius
                // ✅ CORRECTION - utiliser les bonnes couleurs
                val colors = listOf(
                    BreathingInhale,   // #a9e4d7
                    BreathingExhale,   // #2c3e50
                    BreathingHold,     // #eab543
                    Color.White
                )
                val color = colors[index % colors.size]
                val alpha = sin(celebration * 6f + index) * 0.5f + 0.5f
                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = 8f,
                    center = Offset(x.toFloat(), y.toFloat())
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp)
                .padding(horizontal = 32.dp)
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = Strings.congratulations,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = Strings.session_complete,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Text(
                text = Strings.alain_signature,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Button(
                onClick = onNewSession,
                modifier = Modifier.size(width = 200.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    Strings.new_session_button,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}