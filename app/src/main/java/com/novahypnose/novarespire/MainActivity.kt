package com.novahypnose.novarespire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.novahypnose.novarespire.navigation.NovaNavigation
import com.novahypnose.novarespire.ui.theme.NovaRespireTheme

/**
 * MainActivity simplifiée avec Navigation Compose
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ État global de l'application
            var isDarkMode by remember { mutableStateOf(false) }
            var soundEnabled by remember { mutableStateOf(true) }
            var backgroundMusicEnabled by remember { mutableStateOf(false) }

            NovaRespireTheme(
                darkTheme = isDarkMode,
                dynamicColor = false
            ) {
                // ✅ Navigation centralisée
                NovaNavigation(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { isDarkMode = !isDarkMode },
                    soundEnabled = soundEnabled,
                    backgroundMusicEnabled = backgroundMusicEnabled,
                    onToggleSound = { soundEnabled = !soundEnabled },
                    onToggleBackgroundMusic = { backgroundMusicEnabled = !backgroundMusicEnabled }
                )
            }
        }
    }
}