package com.example.mindup.ui.screen.main

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mindup.ui.components.buttons.navbar.NavItem
import com.example.mindup.ui.screen.pages.FichaPage
import com.example.mindup.ui.screen.pages.HomePage
import com.example.mindup.ui.screen.pages.NotificationPage
import com.example.mindup.ui.screen.pages.ProfileNav
import com.example.mindup.ui.screen.pages.QuizPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    val navItemList = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Ficha", Icons.Default.Email),
        NavItem("Quiz", Icons.Default.Search),
        NavItem("Alerta", Icons.Default.Notifications),
        NavItem("Perfil", Icons.Default.Person),
    )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Seguro que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    // Feedback rápido para verificar que sí entra
                    Toast.makeText(context, "Saliendo…", Toast.LENGTH_SHORT).show()
                    onLogout() // <- AQUÍ se ejecuta la navegación al login
                }) {
                    Text("Sí, salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(navItemList[selectedIndex].label) },
                actions = {
                    IconButton(onClick = { showLogoutConfirm = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                        label = { Text(navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = modifier.padding(innerPadding),
            selectedIndex = selectedIndex
        )
    }
}

@Composable
private fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> HomePage()
        1 -> FichaPage()
        2 -> QuizPage()
        3 -> NotificationPage()
        4 -> ProfileNav(modifier)
    }
}