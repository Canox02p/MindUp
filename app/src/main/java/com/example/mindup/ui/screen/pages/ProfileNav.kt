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
fun ProfileNav(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    ProfileView(
        modifier = modifier,
        onEdit = onEdit,
        onLogout = onLogout
    )
}
