package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores desde el Theme (usa MaterialTheme.colorScheme.*)
private val Muted = Color(0xFF7E8CA0)
private val SoftBorder = Color(0xFFE7ECF5)


@Composable
fun FichaPage() {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
        ) {
            // ===== Fichas de estudio =====
            SectionCard {
                MindUpTitle("Fichas de estudio", sizeSp = 20)
                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TagPill(text = "SRS activado")
                    Spacer(Modifier.width(6.dp))
                    Text("•", color = Muted)
                    Spacer(Modifier.width(6.dp))
                    Text("25 tarjetas", color = Muted, fontSize = 13.sp)
                }

                Spacer(Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SolidColor(SoftBorder)),
                    shadowElevation = 0.dp
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text("¿Cuál es el área del círculo?", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(2.dp))
                        Text("A = πr²", color = Muted, fontSize = 13.sp)

                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DifficultyChip("Difícil")
                            DifficultyChip("Medio")
                            DifficultyChip("Fácil", selected = true)
                        }
                    }
                }
            }

            // ===== Plan de estudio =====
            SectionCard {
                MindUpTitle("Plan de estudio", sizeSp = 18)
                Spacer(Modifier.height(6.dp))
                Text("Próximo examen: Química", color = Muted, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                TagPill("Semana 3/6")
            }

            // ===== Progreso semanal =====
            SectionCard {
                MindUpTitle("Progreso semanal", sizeSp = 18)
                Spacer(Modifier.height(10.dp))
                WeeklyBars(values = listOf(4, 6, 5, 7, 3, 4, 6), max = 8)
            }

            // ===== Tareas =====
            SectionCard {
                MindUpTitle("Tareas", sizeSp = 18)
                Spacer(Modifier.height(10.dp))

                var t1 by remember { mutableStateOf(false) }
                var t2 by remember { mutableStateOf(false) }

                TaskRow(checked = t1, onCheckedChange = { t1 = it }, text = "Revisar clase 10%")
                Spacer(Modifier.height(8.dp))
                TaskRow(checked = t2, onCheckedChange = { t2 = it }, text = "Completar prueba práctica")

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = SoftBorder)
                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetaText("Tarjetas: ", "24")
                    MetaText("Quizzes: ", "5")
                    MetaText("Racha: ", "8")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
private fun TagPill(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFEFF7FF),
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            color = Color(0xFF167ABF),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun DifficultyChip(text: String, selected: Boolean = false) {
    val bg = if (selected) Color(0xFFDBF4FF) else Color(0xFFF4F7FB)
    val fg = if (selected) Color(0xFF007ACC) else MaterialTheme.colorScheme.onSurface
    Surface(
        color = bg,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun WeeklyBars(values: List<Int>, max: Int) {
    val barColor = Color(0xFFFFC94C)
    val baseline = SoftBorder
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp)
    ) {
        val w = size.width
        val h = size.height
        val bars = values.take(7)
        val spacing = w / (bars.size * 2f + (bars.size - 1))
        val barWidth = spacing
        val startX = spacing / 2f

        drawLine(
            color = baseline,
            start = androidx.compose.ui.geometry.Offset(0f, h - 8f),
            end = androidx.compose.ui.geometry.Offset(w, h - 8f),
            strokeWidth = 3f
        )

        bars.forEachIndexed { i, v ->
            val pct = (v.toFloat() / max.coerceAtLeast(1)).coerceIn(0f, 1f)
            val barHeight = (h * 0.65f) * pct
            val x = startX + i * (barWidth * 2f + spacing / 2f)
            val y = h - 8f - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
            )
        }
    }
}

@Composable
private fun TaskRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit, text: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(Modifier.width(8.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun MetaText(prefix: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(prefix, color = Muted, fontSize = 12.sp)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
