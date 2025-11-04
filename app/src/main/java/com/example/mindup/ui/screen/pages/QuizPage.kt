package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Paleta rápida alineada al resto
private val PageBg = Color(0xFFF4F7FB)
private val CardBg = Color.White
private val Navy   = Color(0xFF1E2746)
private val Muted  = Color(0xFF7E8CA0)
private val PrimaryBlue = Color(0xFF2F6BFF)

data class QuizOption(val id: Int, val text: String)
data class QuizQuestion(
    val title: String = "Mini Quiz",
    val prompt: String = "¿Cuál es la derivada de x^2?",
    val options: List<QuizOption> = listOf(
        QuizOption(1, "2x"),
        QuizOption(2, "x"),
        QuizOption(3, "x²")
    ),
    val correctId: Int = 1
)

@Composable
fun QuizPage(
    modifier: Modifier = Modifier,
    question: QuizQuestion = QuizQuestion(),
    onResult: (isCorrect: Boolean) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    Scaffold(
        containerColor = PageBg,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        Column(
            modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = inner.calculateStartPadding(LayoutDirection.Ltr),
                        end   = inner.calculateEndPadding(LayoutDirection.Ltr),
                        bottom= inner.calculateBottomPadding()
                    )
                )
                .verticalScroll(rememberScrollState())
        ) {
            // Tarjeta principal del quiz
            Surface(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                color = CardBg,
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 2.dp
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        question.title,
                        fontSize = 20.sp,
                        color = Navy,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        question.prompt,
                        color = Muted,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))

                    var selected by remember { mutableStateOf<Int?>(null) }
                    question.options.forEach { opt ->
                        QuizRadio(
                            text = opt.text,
                            selected = selected == opt.id,
                            onClick = { selected = opt.id }
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (selected == null) {
                                scope.launch { snackbar.showSnackbar("Selecciona una opción") }
                                return@Button
                            }
                            val ok = selected == question.correctId
                            onResult(ok)
                            scope.launch { snackbar.showSnackbar(if (ok) "¡Correcto!" else "Respuesta incorrecta") }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Enviar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuizRadio(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF7F9FC),
        shadowElevation = 0.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryBlue,
                    unselectedColor = Muted
                )
            )
            Spacer(Modifier.width(6.dp))
            Text(text, color = Navy)
        }
    }
}

@Composable
private fun QuizTopBar() {
    Surface(color = Color(0xFFE9F1FF)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MindUp", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Box(
                Modifier.size(36.dp).clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountCircle, null, tint = Muted, modifier = Modifier.size(32.dp))
            }
        }
    }
}
