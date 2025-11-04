package com.example.mindup.ui.screen.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun ProfileNav(
    onLogout: () -> Unit
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = "profile/view"
    ) {
        composable("profile/view") {
            ProfileView(
                onEdit = { nav.navigate("profile/edit") },
                onLogout = onLogout
            )
        }

        composable("profile/edit") {
            ProfileEditPage(
                onBack = { nav.popBackStack() }
            )
        }
    }
}
