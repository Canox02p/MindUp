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
import com.example.mindup.ui.screen.main.MainViewModel // 👈 Importamos el nuevo ViewModel
import com.example.mindup.ui.screen.recover.RecoverScreen
import com.example.mindup.ui.screen.register.RegisterScreen
import com.example.mindup.ui.screen.register.RegisterViewModel
import com.example.mindup.ui.screen.welcome.SplashVideoScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Inicializamos dependencias
    val prefs = remember { UserPrefs(context) }
    val repo = remember { UserRepository(prefs) }
    val scope = rememberCoroutineScope()

    // 1. LOGIN ViewModel (Conectado a Prefs)
    val loginVm: LoginViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(prefs) as T
            }
        }
    )

    // 2. REGISTER ViewModel (Conectado a Repo - si no lo has cambiado)
    val registerVm: RegisterViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(repo) as T
            }
        }
    )

    // 👇 3. MAIN ViewModel (¡NUEVO! Conectado a Prefs para sacar el Token)
    val mainVm: MainViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(prefs) as T
            }
        }
    )

    val logged by prefs.isLoggedIn.collectAsState(initial = false)

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashVideoScreen {
                navController.navigate(if (logged) "main" else "login") {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
        }

        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onLoginOk = {
                    scope.launch { prefs.setLoggedIn(true) }
                    navController.navigate("main") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoRegister = { navController.navigate("register") { launchSingleTop = true } },
                onForgotPassword = { navController.navigate("recover") { launchSingleTop = true } }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = registerVm,
                onRegisterSuccess = {
                    scope.launch { prefs.setLoggedIn(true) }
                    navController.navigate("main") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        composable("recover") {
            RecoverScreen(
                repo = repo,
                onRecovered = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable("main") {
            // 👇 Aquí le pasamos el mainVm a la pantalla principal
            MainScreen(
                viewModel = mainVm, // <-- ¡Importante! Asegúrate que MainScreen reciba esto
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                    scope.launch {
                        prefs.setLoggedIn(false)
                        prefs.clearAccount()
                    }
                }
            )
        }
    }
}