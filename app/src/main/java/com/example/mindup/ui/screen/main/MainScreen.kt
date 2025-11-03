package com.example.mindup.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.mindup.ui.components.buttons.navbar.NavItem
import com.example.mindup.ui.screen.pages.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}   // se usará solo en Perfil
) {
    val navItemList = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Fichas", Icons.Default.Email),
        NavItem("Quiz", Icons.Default.Search),
        NavItem("Alertas", Icons.Default.Notifications),
        NavItem("Perfil", Icons.Default.Person),
    )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(navItemList[selectedIndex].label) }

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
        when (selectedIndex) {
            0 -> HomePage(modifier = modifier.padding(innerPadding))
            1 -> FichaPage()
            2 -> QuizPage()
            3 -> NotificationPage()
            4 -> ProfileView(
                modifier = modifier.padding(innerPadding),
                onEdit = { /* navega a tu editor si quieres */ },
                onLogout = onLogout
            )
        }
    }
}
