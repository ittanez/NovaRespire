package com.novahypnose.novarespire.ui.screens

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.novahypnose.novarespire.model.BreathingSessionViewModel
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.data.models.Phase
import com.novahypnose.novarespire.data.models.SessionState
import com.novahypnose.novarespire.ui.components.BreathingGuide
import com.novahypnose.novarespire.utils.Strings
import com.novahypnose.novarespire.R
import kotlin.math.sin

@Composable
fun SessionScreen(
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
    val viewModel: BreathingSessionViewModel = hiltViewModel()
    val sessionState by viewModel.sessionState.collectAsState()

    LaunchedEffect(exercise, duration) {
        Log.d("SessionScreen", "🚀 Démarrage session: ${exercise.name}")
        viewModel.startSession(exercise, duration)
    }

    LaunchedEffect(sessionState) {
        when (sessionState) {
            is SessionState.Completed -> {
                Log.d("SessionScreen", "✅ Session terminée avec succès")
                onComplete()
            }
            is SessionState.Stopped -> {
                Log.d("SessionScreen", "🛑 Session arrêtée par l'utilisateur")
                onStop()
            }
            else -> {
                // Autres états - ne rien faire
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SessionBackgroundWithEffects(isDarkMode = isDarkMode)

        when (val state = sessionState) {
            is SessionState.Idle -> {
                SessionLoadingUI()
            }
            is SessionState.Countdown -> {
                SessionCountdownUI(secondsLeft = state.secondsLeft)
            }
            is SessionState.Active -> {
                SessionActiveUI(
                    state = state,
                    exerciseName = exercise.name,
                    isDarkMode = isDarkMode,
                    soundEnabled = soundEnabled,
                    onToggleSound = onToggleSound,
                    onTogglePause = { viewModel.togglePause() },
                    onStop = {
                        viewModel.stopSession()
                        onStop()
                    }
                )
            }
            is SessionState.Paused -> {
                SessionPausedUI(
                    pausedState = state,
                    isDarkMode = isDarkMode,
                    onResume = { viewModel.togglePause() },
                    onStop = {
                        viewModel.stopSession()
                        onStop()
                    }
                )
            }
            is SessionState.Completed -> {
                SessionCompletedUI()
            }
            is SessionState.Stopped -> {
                SessionStoppedUI()
            }
        }
    }
}

@Composable
private fun SessionBackgroundWithEffects(isDarkMode: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ IMAGE QUI MARCHE
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (isDarkMode) 0.6f else 0.8f
        )

        // Effets de fond
        SessionBackgroundEffects()
    }
}

@Composable
private fun SessionBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundEffects")

    val bubbleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbles"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(8) { index ->
            val bubbleSize = 20f + (index % 3) * 8f
            val x = (index * 120f + sin(bubbleOffset * 1.2f + index) * 30f) % size.width
            val y = size.height - (bubbleOffset * (size.height + 200f)) + (index * 150f) % 300f
            if (y > -100f && y < size.height + 100f) {
                val alpha = 0.06f + sin(bubbleOffset * 1.8f + index) * 0.03f
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = bubbleSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
private fun SessionLoadingUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun SessionCountdownUI(secondsLeft: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.size(200.dp), // ✅ RÉDUIT DE 250dp À 200dp
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (secondsLeft > 0) secondsLeft.toString() else Strings.lets_go,
                    fontSize = if (secondsLeft > 0) 48.sp else 24.sp, // ✅ RÉDUIT
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp) // ✅ AJOUT PADDING
                )
            }
        }
    }
}

@Composable
private fun SessionActiveUI(
    state: SessionState.Active,
    exerciseName: String,
    isDarkMode: Boolean,
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onTogglePause: () -> Unit,
    onStop: () -> Unit
) {
    var showControls by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        SessionHeader(
            sessionTimeRemaining = state.sessionTimeRemaining,
            currentPhase = state.currentPhase,
            phaseTimeRemaining = state.phaseTimeRemaining,
            sessionProgress = state.getSessionProgress(state.sessionTimeRemaining + 60)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { showControls = !showControls } // Toggle au tap
        ) {
            BreathingGuide(
                currentPhase = state.currentPhase,
                isActive = true,
                phaseProgress = state.phaseProgress,
                phaseTimeRemaining = state.phaseTimeRemaining,
                isDarkMode = isDarkMode,
                exerciseName = "", // ✅ GARDEZ VIDE
                techniqueName = exerciseName // ✅ SEULEMENT LE NOM DE L'EXERCICE
            )
        }

        // Boutons masqués par défaut, apparaissent au tap
        if (showControls) {
            SessionControls(
                soundEnabled = soundEnabled,
                isPaused = false,
                onToggleSound = onToggleSound,
                onTogglePause = onTogglePause,
                onStop = onStop
            )
        }
    }
}

@Composable
private fun SessionPausedUI(
    pausedState: SessionState.Paused,
    isDarkMode: Boolean,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⏸️",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = Strings.pause_text,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Session en pause",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onResume,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.size(width = 120.dp, height = 48.dp)
                    ) {
                        Text(
                            Strings.resume_button,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    OutlinedButton(
                        onClick = onStop,
                        modifier = Modifier.size(width = 120.dp, height = 48.dp)
                    ) {
                        Text(
                            Strings.stop_button,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionCompletedUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Session terminée !",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun SessionStoppedUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Session arrêtée",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SessionHeader(
    sessionTimeRemaining: Int,
    currentPhase: Phase,
    phaseTimeRemaining: Int,
    sessionProgress: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp
            )
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Session: ${Strings.formatTime(sessionTimeRemaining)}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp),
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.7f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )

        // ✅ LINEARPROGRESSINDICATOR CORRIGÉ
        LinearProgressIndicator(
            progress = sessionProgress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Phase: ${currentPhase.instruction} (${phaseTimeRemaining}s)",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.7f),
                    offset = Offset(1f, 1f),
                    blurRadius = 3f
                )
            )
        )
    }
}

@Composable
private fun SessionControls(
    soundEnabled: Boolean,
    isPaused: Boolean,
    onToggleSound: () -> Unit,
    onTogglePause: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding() + 32.dp
            )
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        FloatingActionButton(
            onClick = onToggleSound,
            containerColor = if (soundEnabled)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            else
                MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(56.dp)
        ) {
            Text(
                text = if (soundEnabled) "🔊" else "🔇",
                fontSize = 20.sp
            )
        }

        FloatingActionButton(
            onClick = onTogglePause,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(56.dp)
        ) {
            Text(
                text = if (isPaused) "▶️" else "⏸️",
                fontSize = 20.sp
            )
        }

        FloatingActionButton(
            onClick = onStop,
            containerColor = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(56.dp)
        ) {
            Text(
                text = "⏹️",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}