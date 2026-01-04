package com.tapsave.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tapsave.app.ui.dashboard.DashboardScreen
import com.tapsave.app.ui.onboarding.AppSelectionScreen
import com.tapsave.app.ui.onboarding.PermissionsScreen
import com.tapsave.app.ui.onboarding.WelcomeScreen

@Composable
fun TapSaveNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.AppSelection.route)
                }
            )
        }

        composable(Screen.AppSelection.route) {
            AppSelectionScreen(
                onContinue = {
                    navController.navigate(Screen.Permissions.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Permissions.route) {
            PermissionsScreen(
                onComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        // Clear backstack - can't go back to onboarding
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
    }
}