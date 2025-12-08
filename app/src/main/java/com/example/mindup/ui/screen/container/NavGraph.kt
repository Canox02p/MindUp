package com.example.mindup.ui.screen.container

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindup.data.model.Materia
import com.example.mindup.data.prefs.UserPrefs
import com.example.mindup.data.repository.UserRepository
import com.example.mindup.ui.screen.login.LoginScreen
import com.example.mindup.ui.screen.login.LoginViewModel
import com.example.mindup.ui.screen.main.MainScreen
import com.example.mindup.ui.screen.main.MainViewModel
import com.example.mindup.ui.screen.pages.SelectMateriaPage
import com.example.mindup.ui.screen.recover.RecoverScreen
import com.example.mindup.ui.screen.register.RegisterScreen
import com.example.mindup.ui.screen.register.RegisterViewModel
import com.example.mindup.ui.screen.welcome.SplashVideoScreen
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    val prefs = remember { UserPrefs(context) }
    val repo = remember { UserRepository(prefs) }
    val scope = rememberCoroutineScope()

    /* =================== LOGIN VIEWMODEL =================== */
    val loginVm: LoginViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(prefs) as T
        }
    })

    /* ================= REGISTER VIEWMODEL ================== */
    val registerVm: RegisterViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(repo) as T
        }
    })

    /* ================== MAIN VIEWMODEL ================== */
    val mainVm: MainViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(prefs) as T
        }
    })

    val logged by prefs.isLoggedIn.collectAsState(initial = false)
    val selectedMateriaName by prefs.selectedMateriaName.collectAsState(initial = "Mis Cursos")

    /* ================== RUTAS ================== */
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        /* -------- SPLASH -------- */
        composable("splash") {
            SplashVideoScreen {
                val next = when {
                    !logged -> "login"
                    logged && selectedMateriaName == "Mis Cursos" -> "selectMateria"
                    else -> "main"
                }
                navController.navigate(next) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
        }

        /* -------- LOGIN -------- */
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onLoginOk = {
                    scope.launch { prefs.setLoggedIn(true) }
                    navController.navigate("selectMateria") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoRegister = { navController.navigate("register") },
                onForgotPassword = { navController.navigate("recover") }
            )
        }

        /* -------- REGISTER -------- */
        composable("register") {
            RegisterScreen(
                viewModel = registerVm,
                onRegisterSuccess = {
                    scope.launch { prefs.setLoggedIn(true) }
                    navController.navigate("selectMateria") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        /* -------- RECOVER -------- */
        composable("recover") {
            RecoverScreen(
                repo = repo,
                onRecovered = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        /* -------- SELECT MATERIA -------- */
        composable("selectMateria") {

            val materiasDemo = listOf(
                Materia(1, "Álgebra Básica", "Conceptos esenciales"),
                Materia(2, "Geometría", "Figuras y áreas"),
                Materia(3, "Cálculo Introductorio", "Límites y derivadas"),
                Materia(4, "Estadística", "Probabilidad y datos")
            )

            SelectMateriaPage(
                materias = materiasDemo,
                onMateriaSelected = { materia ->
                    scope.launch {
                        prefs.saveSelectedMateria(materia.nombre, materia.id)
                    }
                    navController.navigate("main") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        /* -------- MAIN -------- */
        composable("main") {
            MainScreen(
                viewModel = mainVm,
                prefs = prefs,          // 👈 IMPORTANTE: pasamos prefs al Home
                onLogout = {
                    scope.launch { prefs.clearAccount() }
                    navController.navigate("login") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
