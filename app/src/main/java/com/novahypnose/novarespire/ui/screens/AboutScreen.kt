package com.novahypnose.novarespire.ui.screens

import android.content.Context
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.draw.shadow  // ✅ AJOUTEZ CELUI-CI
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import com.novahypnose.novarespire.R
import com.novahypnose.novarespire.utils.Strings
import kotlin.math.*

/**
 * Écran À propos COMPLET avec photo d'Alain Zenatti
 */
@Composable
fun AboutScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Arrière-plan avec effets
        AboutScreenBackgroundWithEffects(isDarkMode = isDarkMode)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {

            // En-tête avec bouton retour
            AboutScreenHeader(onBack = onBack)

            // Contenu principal
            AboutScreenContent(
                context = context,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

// ========================================
// ARRIÈRE-PLAN AVEC EFFETS ✨
// ========================================

@Composable
private fun AboutScreenBackgroundWithEffects(isDarkMode: Boolean) {
    // Arrière-plan avec dégradé
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isDarkMode) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E),
                            Color(0xFF0F1419)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2),
                            Color(0xFF667eea)
                        )
                    )
                }
            )
    )

    // Effets de fond animés
    AboutBackgroundEffects()
}

@Composable
private fun AboutBackgroundEffects() {
    val infiniteTransition = rememberInfiniteTransition(label = "aboutBackground")
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

// ========================================
// COMPOSANTS DE L'ÉCRAN À PROPOS
// ========================================
@Composable
private fun AboutScreenHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FloatingActionButton(
            onClick = onBack,
            containerColor = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.size(48.dp)
        ) {
            Text("←", fontSize = 20.sp, color = Color.Black)
        }

        Text(
            text = Strings.my_approach,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // ✅ PHOTO ICI - En en-tête
        Card(
            modifier = Modifier.size(48.dp),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.photo_alain),
                contentDescription = "Alain",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun AboutScreenContent(
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ Photo de profil d'Alain Zenatti
        AlainProfilePhoto()

        Spacer(modifier = Modifier.height(24.dp))

        // Biographie d'Alain
        AlainBiographyCard()

        Spacer(modifier = Modifier.height(24.dp))

        // Call to Action et liens
        AlainCTACard(context = context)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ========================================
// PHOTO DE PROFIL ALAIN ZENATTI ✨
// ========================================

@Composable
private fun AlainProfilePhoto() {
    Card(
        modifier = Modifier.size(150.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4A90E2),
                            Color(0xFF667eea)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🧠",
                    fontSize = 48.sp
                )
                Text(
                    text = "AZ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Hypnothérapeute",
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun AlainBiographyCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ PHOTO ICI - Dans la biographie
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo à gauche
                Card(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.photo_alain),
                        contentDescription = "Alain Zenatti",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Nom à droite
                Column {
                    Text(
                        text = "Alain Zenatti",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Maître Hypnologue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4A90E2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Strings.alain_bio,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}






@Composable
private fun AlainCTACard(context: Context) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Message d'encouragement
            Text(
                text = Strings.alain_cta,
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Bouton principal d'action
            Button(
                onClick = {
                    openWebsite(context, "https://novahypnose.fr")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = Strings.discover_coaching,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lien vers le site web
            TextButton(
                onClick = {
                    openWebsite(context, "https://novahypnose.fr")
                }
            ) {
                Text(
                    text = Strings.website,
                    fontSize = 14.sp,
                    color = Color(0xFF4A90E2),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informations de contact supplémentaires
            ContactInformation()
        }
    }
}

@Composable
private fun ContactInformation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A90E2).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📍 Consultation & Accompagnement",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A90E2),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "• Hypnose Ericksonienne\n• Thérapie brève\n• Accompagnement personnalisé",
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

// ========================================
// FONCTIONS UTILITAIRES
// ========================================

/**
 * Ouvre le site web dans le navigateur
 */
private fun openWebsite(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
        Log.d("AboutScreen", "✅ Ouverture du site web: $url")
    } catch (e: Exception) {
        Log.e("AboutScreen", "❌ Erreur ouverture lien: ${e.message}")
    }
}

/**
 * Ouvre l'application email pour contacter Alain
 */
private fun openEmailClient(context: Context, email: String, subject: String = "") {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            Log.d("AboutScreen", "✅ Ouverture client email: $email")
        } else {
            Log.w("AboutScreen", "⚠️ Aucun client email disponible")
        }
    } catch (e: Exception) {
        Log.e("AboutScreen", "❌ Erreur ouverture email: ${e.message}")
    }
}