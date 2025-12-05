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
import com.example.mindup.ui.screen.pages.HomePage
import com.example.mindup.ui.screen.pages.NotificationPage
import com.example.mindup.ui.screen.pages.PlanDetailPage
import com.example.mindup.ui.screen.pages.PracticePage
import com.example.mindup.ui.screen.pages.ProfileNav
import com.example.mindup.ui.screen.pages.QuizPage

private val PageBg  = Color(0xFFFFFFFF)
private val Primary = Color(0xFF2B9FD6)
private val Muted   = Color(0xFF7E8CA0)
private val Navy    = Color(0xFF1B1F23)

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    var quizFromHomeModuleId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PageBg,
        topBar = { HeaderTitleBar(selectedIndex) },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    R.drawable.home       to "Inicio",
                    R.drawable.contenido  to "Contenido",
                    R.drawable.practica   to "Práctica",
                    R.drawable.alerta     to "Alertas",
                    R.drawable.perfil     to "Perfil"
                )
                items.forEachIndexed { i, (iconRes, label) ->
                    val selected = selectedIndex == i
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            selectedIndex = i
                            if (i != 0) quizFromHomeModuleId = null
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = label,
                                modifier = Modifier.size(45.dp),
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
                0 -> {
                    // Si hay módulo seleccionado desde Home -> mostrar Quiz
                    val pending = quizFromHomeModuleId
                    if (pending != null) {
                        QuizPage(
                            onConfirmHome = {
                                // Regresar a Home (lista) al confirmar
                                quizFromHomeModuleId = null
                                selectedIndex = 0
                            }
                        )
                    } else {
                        // Pasamos el callback al primer botón/nodo de Home
                        HomePage(
                            onStartQuiz = { moduleId ->
                                quizFromHomeModuleId = moduleId
                            }
                        )
                    }
                }
                1 -> PlanDetailPage()
                2 -> PracticePage()
                3 -> NotificationPage()
                4 -> ProfileNav(onLogout)
            }
        }
    }
}

@Composable
private fun HeaderTitleBar(selectedIndex: Int) {
    Surface(color = PageBg) {
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val titleText = when (selectedIndex) {
                0 -> "MindUp"
                1 -> "Contenido"
                2 -> "Práctica"
                3 -> "Alertas"
                else -> "Perfil"
            }
            BicolorTitle(titleText)

            Spacer(Modifier.weight(1f))

            if (selectedIndex == 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_mindup),
                    contentDescription = null,
                    modifier = Modifier.size(58.dp)
                )
            }
        }
    }
}

@Composable
private fun BicolorTitle(text: String, size: Int = 28, weight: FontWeight = FontWeight.W700) {
    val first = text.firstOrNull()?.toString().orEmpty()
    val rest  = if (text.length > 1) text.drop(1) else ""
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(color = Navy,   fontSize = size.sp, fontWeight = weight)) { append(first) }
            withStyle(SpanStyle(color = Primary, fontSize = size.sp, fontWeight = weight)) { append(rest) }
        }
    )
}
