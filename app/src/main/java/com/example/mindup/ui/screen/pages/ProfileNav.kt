package com.example.mindup.ui.screen.pages

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindup.ui.viewmodel.ProfileViewModel
import com.example.mindup.ui.viewmodel.ProfileViewModelFactory

@Composable
fun ProfileNav(modifier: Modifier = Modifier) {
    val app = LocalContext.current.applicationContext as Application
    val vm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(app))
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = "profile/view",
        modifier = modifier
    ) {
        composable("profile/view") {
            ProfileView(vm = vm, onEdit = { nav.navigate("profile/edit") })
        }
        composable("profile/edit") {
            ProfileEditPage(
                vm = vm,
                onSaved = { nav.popBackStack() },
                onCancel = { nav.popBackStack() }
            )
        }
    }
}