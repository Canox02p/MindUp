package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta rápida
private val PageBg = Color(0xFFF4F7FB)
private val CardBg = Color.White
private val Navy   = Color(0xFF1E2746)
private val Muted  = Color(0xFF7E8CA0)
private val Accent = Color(0xFF2EC5FF)
private val ChipBg = Color(0xFFEFF7FF)
private val ChipText = Color(0xFF167ABF)
private val SoftBorder = Color(0xFFE7ECF5)

@Composable
fun FichaPage() {
    Scaffold(
        containerColor = PageBg,
        topBar = { FichaTopBar() }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            // ===== Sección: Fichas de estudio =====
            SectionCard {
                Text("Fichas de estudio", color = Navy, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TagPill(text = "SRS activado")
                    Spacer(Modifier.width(6.dp))
                    Text("•", color = Muted)
                    Spacer(Modifier.width(6.dp))
                    Text("25 tarjetas", color = Muted, fontSize = 13.sp)
                }

                Spacer(Modifier.height(12.dp))

                // Tarjeta de pregunta
                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(SoftBorder))
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text("¿Cuál es el área del círculo?", color = Navy, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(2.dp))
                        Text("A = πr²", color = Muted, fontSize = 13.sp)

                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DifficultyChip(text = "Difícil")
                            DifficultyChip(text = "Medio")
                            DifficultyChip(text = "Fácil", selected = true)
                        }
                    }
                }
            }

            // ===== Sección: Plan de estudio =====
            SectionCard {
                Text("Plan de estudio", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(6.dp))
                Text("Próximo examen: Química", color = Muted, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                TagPill(text = "Semana 3/6")
            }

            // ===== Sección: Progreso semanal =====
            SectionCard {
                Text("Progreso semanal", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(10.dp))
                WeeklyBars(
                    values = listOf(4, 6, 5, 7, 3, 4, 6), // 0-7
                    max = 8
                )
            }

            // ===== Sección: Tareas =====
            SectionCard {
                Text("Tareas", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(10.dp))

                var t1 by remember { mutableStateOf(false) }
                var t2 by remember { mutableStateOf(false) }

                TaskRow(checked = t1, onCheckedChange = { t1 = it }, text = "Revisar clase 10%")
                Spacer(Modifier.height(8.dp))
                TaskRow(checked = t2, onCheckedChange = { t2 = it }, text = "Completar prueba práctica")

                Spacer(Modifier.height(12.dp))
                Divider(color = SoftBorder)
                Spacer(Modifier.height(8.dp))
                // Métricas
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

/* ---------- TopBar ---------- */

@Composable
private fun FichaTopBar() {
    Surface(color = PageBg) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MindUp", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            // Circulito con check/onda (decorativo)
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF7FF)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(Modifier.size(28.dp)) {
                    drawCircle(color = Accent, style = Stroke(width = 4f))
                    // mini “check/onda”
                    val w = size.width
                    val h = size.height
                    drawArc(
                        color = Accent,
                        startAngle = 200f,
                        sweepAngle = 140f,
                        useCenter = false,
                        style = Stroke(width = 6f)
                    )
                }
            }
            // logo mini
            Surface(
                shape = CircleShape,
                color = CardBg,
                shadowElevation = 1.dp
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Muted,
                    modifier = Modifier.size(36.dp).padding(2.dp)
                )
            }
        }
    }
}

/* ---------- Helpers visuales ---------- */

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardBg,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
private fun TagPill(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = ChipBg,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            color = ChipText,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun DifficultyChip(text: String, selected: Boolean = false) {
    val bg = if (selected) Color(0xFFDBF4FF) else Color(0xFFF4F7FB)
    val fg = if (selected) Color(0xFF007ACC) else Navy
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
    // Canvas simple con 7 barras redondeadas
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
        val spacing = w / (bars.size * 2f + (bars.size - 1)) // ancho y separación balanceados
        val barWidth = spacing
        val startX = spacing / 2f

        // línea base sutil
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
        Text(text, color = Navy)
    }
}

@Composable
private fun MetaText(prefix: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(prefix, color = Muted, fontSize = 12.sp)
        Text(value, color = Navy, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
