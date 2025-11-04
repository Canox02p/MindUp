package com.example.mindup.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.mindup.R
import com.example.mindup.ui.screen.pages.FichaPage
import com.example.mindup.ui.screen.pages.HomePage
import com.example.mindup.ui.screen.pages.NotificationPage
import com.example.mindup.ui.screen.pages.ProfileNav
import com.example.mindup.ui.screen.pages.QuizPage

private val PageBg  = Color(0xFFEAF2FF)
private val Primary = Color(0xFF2B9FD6)
private val Muted   = Color(0xFF7E8CA0)
private val Navy    = Color(0xFF1B1F23)

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = { HeaderTitleBar() },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    R.drawable.home   to "Inicio",
                    R.drawable.ficha  to "Fichas",
                    R.drawable.quiz   to "Quiz",
                    R.drawable.alerta to "Alertas",
                    R.drawable.perfil to "Perfil"
                )

                items.forEachIndexed { i, (iconRes, label) ->
                    val selected = selectedIndex == i
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedIndex = i },
                        icon = {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = label,
                                modifier = Modifier.size(35.dp),
                                tint = if (selected) Primary else Muted
                            )

                        },
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
                4 -> ProfileNav(onLogout)
            }
        }
    }
}

@Composable
private fun HeaderTitleBar() {
    Surface(color = PageBg) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
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
                modifier = Modifier.size(58.dp)
            )
        }
    }
}

