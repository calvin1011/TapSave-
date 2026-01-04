package com.tapsave.app.ui.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object AppSelection : Screen("app_selection")
    data object Permissions : Screen("permissions")
    data object Dashboard : Screen("dashboard")
}