package com.example.mindup.ui.screen.container

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindup.data.prefs.UserPrefs
import com.example.mindup.data.repository.UserRepository
import com.example.mindup.ui.screen.login.LoginScreen
import com.example.mindup.ui.screen.login.LoginViewModel
import com.example.mindup.ui.screen.main.MainScreen
import com.example.mindup.ui.screen.welcome.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context) }
    val repo = remember { UserRepository() }
    val scope = rememberCoroutineScope()

    // ViewModel de Login
    val loginVm: LoginViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repo, prefs) as T
            }
        }
    )

    // Observa el estado de sesión para decidir desde el Splash
    val logged by prefs.isLoggedIn.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // SPLASH: decide adónde ir cuando termina la animación
        composable("splash") {
            SplashScreen {
                navController.navigate(if (logged) "main" else "login") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        // LOGIN: al autenticar, navega a MAIN (y limpia back stack)
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onLoginOk = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // MAIN: permite cerrar sesión y regresa a LOGIN
        composable("main") {
            MainScreen(
                onLogout = {
                    scope.launch {
                        prefs.setLoggedIn(false)
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

