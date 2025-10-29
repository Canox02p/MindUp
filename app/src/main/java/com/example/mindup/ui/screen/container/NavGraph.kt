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
import com.example.mindup.ui.screen.welcome.SplashVideoScreen
import com.example.mindup.ui.screen.register.RegisterScreen
import com.example.mindup.ui.screen.register.RegisterViewModel
import com.example.mindup.ui.screen.recover.RecoverScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context) }
    val repo = remember { UserRepository(prefs) } // si tu repo requiere prefs: UserRepository(prefs)
    val scope = rememberCoroutineScope()

    // --- ViewModel de Login ---
    val loginVm: LoginViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(repo, prefs) as T
            }
        }
    )

    // --- ViewModel de Registro ---
    val registerVm: RegisterViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(repo) as T
            }
        }
    )

    // Estado para decidir auto-login desde Splash (si quieres mantenerlo)
    val logged by prefs.isLoggedIn.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 🔹 SPLASH (video): decide login o main
        composable("splash") {
            SplashVideoScreen {
                navController.navigate(if (logged) "main" else "login") {
                    popUpTo(0)            // limpia TODO el back stack
                    launchSingleTop = true
                }
            }
        }

        // 🔹 LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onLoginOk = {
                    navController.navigate("main") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoRegister = {
                    navController.navigate("register") {
                        launchSingleTop = true
                    }
                },
                onForgotPassword = {
                    navController.navigate("recover") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🔹 REGISTRO (azul cielo) — sobrescribe cuenta si ya existe
        composable("register") {
            RegisterScreen(
                vm = registerVm,
                onRegisteredOk = {
                    // Al registrar, entra directo al main
                    navController.navigate("main") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onBackToLogin = {
                    navController.popBackStack() // regresar al login
                }
            )
        }

        // 🔹 RECUPERAR CONTRASEÑA
        composable("recover") {
            RecoverScreen(
                repo = repo,
                onRecovered = {
                    // al actualizar contraseña, vuelve al login
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 🔹 MAIN (con logout funcional)
        composable("main") {
            MainScreen(
                onLogout = {
                    // 1) Navega primero (para evitar carreras con IO)
                    navController.navigate("login") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                    // 2) Limpia sesión en segundo plano
                    scope.launch { prefs.setLoggedIn(false) }
                }
            )
        }
    }
}

