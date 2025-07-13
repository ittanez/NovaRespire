package com.novahypnose.novarespire.ui.screens

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.novahypnose.novarespire.R
import com.novahypnose.novarespire.utils.Strings
import com.novahypnose.novarespire.ui.theme.NovaColors  // ✅ IMPORT AJOUTÉ
import kotlin.math.*

/**
 * Page dédiée au profil d'Alain Zenatti avec NovaColors
 */
@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Arrière-plan avec NovaColors
        ProfileBackground(isDarkMode = isDarkMode)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {

            // ✅ En-tête avec NovaColors
            ProfileHeader(onBack = onBack)

            // ✅ Contenu avec NovaColors
            ProfileContent(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                context = context
            )
        }
    }
}

@Composable
private fun ProfileBackground(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isDarkMode) {
                    Brush.linearGradient(colors = NovaColors.darkGradient()) // ✅ NOVA COLORS
                } else {
                    Brush.linearGradient(colors = NovaColors.primaryGradient()) // ✅ NOVA COLORS
                }
            )
    )

    // ✅ Effets de fond avec NovaColors
    ProfileBackgroundEffects()
}

@Composable
private fun ProfileBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "profileBackground")
    val sparkleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkles"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(12) { index ->
            val sparkleSize = 15f + (index % 4) * 5f
            val x = (index * 100f + sin(sparkleOffset * 1.3f + index) * 40f) % size.width
            val y = size.height - (sparkleOffset * (size.height + 300f)) + (index * 200f) % 400f

            if (y > -150f && y < size.height + 150f) {
                val alpha = 0.04f + sin(sparkleOffset * 2.2f + index) * 0.02f
                drawCircle(
                    color = NovaColors.White.copy(alpha = alpha), // ✅ NOVA COLORS
                    radius = sparkleSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(onBack: () -> Unit) {
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
            text = "Mon approche",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NovaColors.White, // ✅ NOVA COLORS
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    context: android.content.Context
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ Photo principale
        ProfileMainPhoto()

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Contenu avec NovaColors
        ProfileAboutCard(context = context)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileMainPhoto() {
    Card(
        modifier = Modifier
            .size(200.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFeab543).copy(alpha = 0.3f),
                        Color(0xFFeab543).copy(alpha = 0.1f)
                    )
                ),
                shape = CircleShape
            ),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // ✅ VRAIE PHOTO D'ALAIN ACTIVÉE !
            Image(
                painter = painterResource(id = R.drawable.photo_alain),
                contentDescription = "Photo d'Alain Zenatti - Hypnothérapeute",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ProfileAboutCard(context: android.content.Context) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ Texte biographique avec NovaColors
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFeab543).copy(alpha = 0.2f),
                            Color(0xFFeab543).copy(alpha = 0.1f),
                            Color(0xFFeab543).copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = Strings.alain_bio,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.8f), // TEXTE NOIR LISIBLE
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(24.dp)
            )
        }

        // ✅ Texte CTA avec NovaColors
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFeab543).copy(alpha = 0.3f),
                            Color(0xFFeab543).copy(alpha = 0.15f),
                            Color(0xFFeab543).copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.97f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Strings.alain_cta,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.8f), // TEXTE NOIR LISIBLE
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // ✅ Bouton avec NovaColors
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://novahypnose.fr"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("ProfileScreen", "Erreur ouverture lien: ${e.message}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFeab543) // ORANGE DORÉ COMME LES AUTRES BOUTONS
                    )
                ) {
                    Text(
                        text = Strings.discover_coaching,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NovaColors.White // ✅ NOVA COLORS
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ Lien site web avec NovaColors
                TextButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://novahypnose.fr"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("ProfileScreen", "Erreur ouverture lien: ${e.message}")
                        }
                    }
                ) {
                    Text(
                        text = Strings.website,
                        fontSize = 14.sp,
                        color = NovaColors.CTA, // ✅ NOVA COLORS - #874c4a
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}