package com.ian.grithaul.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ian.grithaul.ui.screens.admin.AdminDashboardScreen
import com.ian.grithaul.ui.screens.auth.LoginScreen
import com.ian.grithaul.ui.screens.auth.RegisterScreen
import com.ian.grithaul.ui.screens.driver.DriverDashboardScreen
import com.ian.grithaul.ui.screens.notification.NotificationsScreen
import com.ian.grithaul.ui.screens.onboarding.OnboardingScreen
import com.ian.grithaul.ui.screens.onboarding.OnboardingScreen1
import com.ian.grithaul.ui.screens.onboarding.OnboardingScreen2
import com.ian.grithaul.ui.screens.profile.ProfileScreen
import com.ian.grithaul.ui.screens.resident.ConfirmPickupScreen
import com.ian.grithaul.ui.screens.resident.FeedbackScreen
import com.ian.grithaul.ui.screens.resident.OnDemandRequestScreen
import com.ian.grithaul.ui.screens.resident.ResidentHomeScreen
import com.ian.grithaul.ui.screens.splash.SplashScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_ONBOARDING) {
            OnboardingScreen(navController)
        }
        composable(ROUT_ONBOARDING1) {
            OnboardingScreen1(navController)
        }
        composable(ROUT_ONBOARDING2) {
            OnboardingScreen2(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_DRIVER_DASHBOARD) {
            DriverDashboardScreen(navController)
        }
        composable(ROUT_ADMIN_DASHBOARD) {
            AdminDashboardScreen(navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }
        composable(ROUT_NOTIFICATIONS) {
            NotificationsScreen(navController)
        }

        composable(ROUT_CONFIRM_PICKUP) {
            ConfirmPickupScreen(navController)
        }
        composable(ROUT_FEEDBACK) {
            FeedbackScreen(navController)
        }
        composable(ROUT_DEMAND_REQUEST) {
            OnDemandRequestScreen(navController)
        }
        composable(ROUT_RESIDENT_HOME) {
            ResidentHomeScreen(navController)
        }


    }
}