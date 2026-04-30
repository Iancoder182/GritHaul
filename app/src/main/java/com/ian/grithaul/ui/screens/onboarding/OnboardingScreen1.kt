package com.ian.grithaul.ui.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ian.grithaul.navigation.ROUT_LOGIN
import com.ian.grithaul.navigation.ROUT_ONBOARDING
import com.ian.grithaul.navigation.ROUT_ONBOARDING2
import com.ian.grithaul.ui.theme.BackgroundLight
import com.ian.grithaul.ui.theme.BorderLight
import com.ian.grithaul.ui.theme.PrimaryGreen
import com.ian.grithaul.ui.theme.TextPrimary
import com.ian.grithaul.ui.theme.TextSecondary
import com.ian.grithaul.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen1(navController: NavController) {

    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.92f) }

    LaunchedEffect(true) {
        launch {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            scaleAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Skip button — top right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alphaAnim.value),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    navController.navigate(ROUT_LOGIN) {
                        popUpTo(ROUT_ONBOARDING) { inclusive = true }
                    }
                }
            ) {
                Text(
                    text = "Skip",
                    color = PrimaryGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Illustration + Title + Description
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Illustration box
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(PrimaryGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🚛",
                    fontSize = 90.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = "Request On Demand",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Need a special collection outside your schedule? Request a pickup anytime and track it from approval to completion.",
                fontSize = 15.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Bottom section — dots + button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Progress dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dot 1 — inactive
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BorderLight)
                )
                // Dot 2 — active
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PrimaryGreen)
                )
                // Dot 3 — inactive
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BorderLight)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Next button
            Button(
                onClick = {
                    navController.navigate(ROUT_ONBOARDING2)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                )
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreen1Preview() {
    OnboardingScreen1(rememberNavController())
}