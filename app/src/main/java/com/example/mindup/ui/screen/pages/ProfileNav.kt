package com.example.mindup.ui.screen.pages

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindup.ui.viewmodel.ProfileViewModel
import com.example.mindup.ui.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileNav(
    onLogout: () -> Unit
) {
    val nav = rememberNavController()

    // Obtenemos el AndroidViewModel vía factory
    val app = LocalContext.current.applicationContext as Application
    val profileVm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(app))

    NavHost(
        navController = nav,
        startDestination = "profile/view"
    ) {
        composable("profile/view") {
            ProfileView(
                viewModel = profileVm,
                onEdit = { nav.navigate("profile/edit") },
                onLogout = onLogout
            )
        }
        composable("profile/edit") {
            ProfileEditPage(
                viewModel = profileVm,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
