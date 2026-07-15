package com.aliexpressclone.app.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
