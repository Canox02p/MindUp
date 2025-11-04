package com.example.mindup.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.mindup.R
import com.example.mindup.ui.screen.pages.FichaPage
import com.example.mindup.ui.screen.pages.HomePage
import com.example.mindup.ui.screen.pages.NotificationPage
import com.example.mindup.ui.screen.pages.ProfileNav
import com.example.mindup.ui.screen.pages.QuizPage

// ======= Paleta local =======
private val PageBg  = Color(0xFFEAF2FF)
private val Primary = Color(0xFF2B9FD6)
private val Muted   = Color(0xFF7E8CA0)
private val Navy    = Color(0xFF1B1F23)

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedIndex by rememberSaveable { mutableStateOf(4) } // Perfil por defecto

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = { HeaderTitleBar() }, // ✅ ahora respeta la barra de estado
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,   // ✅ blanco
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    Icons.Filled.Home to "Inicio",
                    Icons.Filled.Mail to "Fichas",
                    Icons.Filled.Search to "Quiz",
                    Icons.Filled.Notifications to "Alertas",
                    Icons.Filled.Person to "Perfil"
                )
                items.forEachIndexed { i, (icon, label) ->
                    NavigationBarItem(
                        selected = selectedIndex == i,
                        onClick = { selectedIndex = i },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Primary.copy(alpha = 0.15f),
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner)) {
            when (selectedIndex) {
                0 -> HomePage()
                1 -> FichaPage()
                2 -> QuizPage()
                3 -> NotificationPage()
                4 -> ProfileNav(onLogout) // 👤 Perfil con sub-nav
            }
        }
    }
}

// ======= HEADER: título + logo MindUp (con statusBarsPadding) =======
@Composable
private fun HeaderTitleBar() {
    Surface(color = PageBg) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()                 // ✅ deja espacio bajo la barra de estado
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Navy,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W700
                        )
                    ) { append("M") }
                    withStyle(
                        SpanStyle(
                            color = Primary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W700
                        )
                    ) { append("indUp") }
                }
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_logo_mindup),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
