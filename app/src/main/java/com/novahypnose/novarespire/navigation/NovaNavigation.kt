package com.novahypnose.novarespire.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.ui.screens.MainScreen
import com.novahypnose.novarespire.ui.screens.ProfileScreen
import com.novahypnose.novarespire.ui.screens.SessionScreen
import com.novahypnose.novarespire.model.BreathingSessionViewModel

/**
 * Routes de navigation centralisées
 */
object NovaRoutes {
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val SESSION = "session/{exerciseId}/{duration}"
    
    fun sessionRoute(exerciseId: String, duration: Int) = "session/$exerciseId/$duration"
}

/**
 * Navigation principale de l'application
 */
@Composable
fun NovaNavigation(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    soundEnabled: Boolean,
    backgroundMusicEnabled: Boolean,
    onToggleSound: () -> Unit,
    onToggleBackgroundMusic: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NovaRoutes.MAIN
    ) {
        
        // ✅ ÉCRAN PRINCIPAL
        composable(NovaRoutes.MAIN) {
            MainScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onStartSession = { exercise, duration ->
                    navController.navigate(
                        NovaRoutes.sessionRoute(exercise.id, duration)
                    )
                },
                onLearnMore = {
                    navController.navigate(NovaRoutes.PROFILE)
                },
                onShowExerciseInfo = { exercise ->
                    // TODO: Implémenter dialogue d'info exercice
                }
            )
        }
        
        // ✅ ÉCRAN PROFIL
        composable(NovaRoutes.PROFILE) {
            ProfileScreen(
                isDarkMode = isDarkMode,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // ✅ ÉCRAN SESSION
        composable(NovaRoutes.SESSION) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            val duration = backStackEntry.arguments?.getString("duration")?.toIntOrNull() ?: 2
            
            // Récupération de l'exercice par ID
            val exercise = Exercise.getAll().find { it.id == exerciseId } ?: Exercise.getDefault()
            
            SessionScreen(
                exercise = exercise,
                duration = duration,
                isDarkMode = isDarkMode,
                soundEnabled = soundEnabled,
                backgroundMusicEnabled = backgroundMusicEnabled,
                onToggleSound = onToggleSound,
                onToggleBackgroundMusic = onToggleBackgroundMusic,
                onComplete = {
                    navController.popBackStack(NovaRoutes.MAIN, inclusive = false)
                },
                onStop = {
                    navController.popBackStack(NovaRoutes.MAIN, inclusive = false)
                }
            )
        }
    }
}