package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

private val Muted = Color(0xFF7E8CA0)

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
        containerColor = MaterialTheme.colorScheme.background,
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
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 2.dp
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Título bicolor
                    MindUpTitle(question.title, sizeSp = 22)

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
                            scope.launch {
                                snackbar.showSnackbar(if (ok) "¡Correcto!" else "Respuesta incorrecta")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
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
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = Muted
                )
            )
            Spacer(Modifier.width(6.dp))
            Text(text, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
