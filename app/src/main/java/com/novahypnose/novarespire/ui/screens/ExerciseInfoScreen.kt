package com.novahypnose.novarespire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novahypnose.novarespire.data.models.Exercise
import com.novahypnose.novarespire.ui.theme.NovaColors

/**
 * Écran d'information détaillée sur un exercice
 */
@Composable
fun ExerciseInfoScreen(
    exercise: Exercise,
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Arrière-plan avec NovaColors
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (isDarkMode) {
                        Brush.linearGradient(colors = NovaColors.darkGradient())
                    } else {
                        Brush.linearGradient(colors = NovaColors.primaryGradient())
                    }
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {

            // ✅ En-tête avec bouton retour
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = onBack,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(
                        text = "←",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Informations",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NovaColors.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // ✅ Contenu principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) {
                        NovaColors.Surface15
                    } else {
                        Color.White.copy(alpha = 0.95f)
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ✅ Nom de l'exercice
                    Text(
                        text = exercise.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) NovaColors.White else Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // ✅ Description
                    Text(
                        text = exercise.description,
                        fontSize = 16.sp,
                        color = if (isDarkMode) NovaColors.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // ✅ Phases de l'exercice
                    Text(
                        text = "Phases de respiration :",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) NovaColors.White else Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    exercise.phases.forEachIndexed { index, phase ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = NovaColors.CTA.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${index + 1}. ${phase.instruction}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkMode) NovaColors.White else Color.Black
                                )
                                Text(
                                    text = "${phase.durationSeconds}s",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NovaColors.CTA
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ✅ Durée totale du cycle
                    val totalCycleDuration = exercise.phases.sumOf { it.durationSeconds }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = NovaColors.CTA.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Durée d'un cycle complet",
                                fontSize = 14.sp,
                                color = if (isDarkMode) NovaColors.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${totalCycleDuration} secondes",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = NovaColors.CTA,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}