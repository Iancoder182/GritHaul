package com.ian.grithaul.ui.screens.splash

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.ian.grithaul.R
import com.ian.grithaul.ui.theme.PrimaryGreen
import com.ian.grithaul.ui.theme.LightGreen
import com.ian.grithaul.ui.theme.White
import com.ian.grithaul.navigation.ROUT_SPLASH

import com.ian.grithaul.navigation.ROUT_LOGIN
import com.ian.grithaul.navigation.ROUT_ONBOARDING
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    // Fade animation
    val alphaAnim = remember { Animatable(0f) }

    // Scale animation
    val scaleAnim = remember { Animatable(0.8f) }

    LaunchedEffect(true) {

        // Run fade and scale simultaneously
        launch {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200)
            )
        }
        launch {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200)
            )
        }

        delay(2500L)

        // Check SharedPreferences for onboarding status
        val prefs = context.getSharedPreferences("grithaul_prefs", Context.MODE_PRIVATE)
        val onboardingComplete = prefs.getBoolean("onboarding_complete", false)

        if (onboardingComplete) {
            // Returning user — go to login
            navController.navigate(ROUT_LOGIN) {
                popUpTo(ROUT_SPLASH) { inclusive = true }
            }
        } else {
            // First time user — go to onboarding
            navController.navigate(ROUT_ONBOARDING) {
                popUpTo(ROUT_SPLASH) { inclusive = true }
            }
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.eco),
            contentDescription = "GritHaul Logo",
            modifier = Modifier
                .size(120.dp)
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // App Name
        Text(
            text = "GritHaul",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = White,
            modifier = Modifier.alpha(alphaAnim.value)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        Text(
            text = "Waste. Managed.",
            style = MaterialTheme.typography.bodyMedium,
            color = LightGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alphaAnim.value)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Supporting tagline
        Text(
            text = "Smart Collection. Real Results.",
            style = MaterialTheme.typography.bodySmall,
            color = White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alphaAnim.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}