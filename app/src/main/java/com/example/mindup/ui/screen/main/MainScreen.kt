package com.example.mindup.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.mindup.ui.components.buttons.navbar.NavItem
import com.example.mindup.ui.screen.pages.FichaPage
import com.example.mindup.ui.screen.pages.HomePage
import com.example.mindup.ui.screen.pages.NotificationPage
import com.example.mindup.ui.screen.pages.ProfileNav
import com.example.mindup.ui.screen.pages.QuizPage

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
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
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { androidx.compose.material3.Icon(navItem.icon, contentDescription = navItem.label) },
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
