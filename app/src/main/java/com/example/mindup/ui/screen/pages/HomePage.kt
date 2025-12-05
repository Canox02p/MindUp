package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// --- PALETA DE COLORES ---
private val PageBg = Color(0xFFF6F9FF)
private val CardBg = Color.White
private val Navy   = Color(0xFF22264C)
private val Aqua   = Color(0xFF03A9F4)

private val ButtonFillLight = Color(0xFFE1F5FE)
private val ButtonBorder    = Color(0xFF4FC3F7)
private val LockedFill      = Color(0xFFF5F5F5)
private val LockedBorder    = Color(0xFFE0E0E0)

enum class ModuleState { LOCKED, AVAILABLE, DONE }
data class Module(val id: Int, val state: ModuleState)

/* ---------------- TOP BAR ---------------- */

@Composable
fun HomeTopBar(
    title: String = "Fundamentos Matemáticos",
    hearts: String = "5",
    coins: String = "140",
    streakDays: Int = 6
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(PageBg)
            .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            color = CardBg,
            shape = RoundedCornerShape(50),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, null, tint = Color(0xFFFF4D4D), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(hearts, color = Navy, style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Diamond, null, tint = Navy, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(coins, color = Navy, style = MaterialTheme.typography.bodyMedium)
                }
                Surface(
                    color = Color(0xFFFFE0B2).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, null, tint = Color(0xFFFF5722), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Racha: $streakDays Días", color = Color(0xFFFF5722), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(shape = RoundedCornerShape(14.dp), color = CardBg, shadowElevation = 1.dp) {
                Icon(Icons.Default.Menu, null, tint = Navy, modifier = Modifier.size(44.dp).padding(10.dp))
            }
            Text(title, color = Navy, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.size(44.dp))
        }
    }
}

/* ---------------- ROADMAP ---------------- */

@Composable
fun RoadMapSection(
    modules: List<Module>,
    onTapModule: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var containerOffsetInRoot by remember { mutableStateOf(Offset.Zero) }
    val centers = remember { mutableStateListOf<Offset>() }

    fun registerCenter(index: Int, centerInRoot: Offset) {
        val local = centerInRoot - containerOffsetInRoot
        while (centers.size <= index) centers.add(Offset.Zero)
        centers[index] = local
    }

    val rowAlignments = listOf(
        Arrangement.Center,
        Arrangement.End,
        Arrangement.Center,
        Arrangement.Start,
        Arrangement.Center
    )

    Box(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .onGloballyPositioned { coords -> containerOffsetInRoot = coords.positionInRoot() }
    ) {
        // Línea conectora
        Canvas(Modifier.matchParentSize().height(640.dp)) {
            if (centers.size >= 2 && centers.all { it != Offset.Zero }) {
                val path = Path()
                path.moveTo(centers[0].x, centers[0].y)
                for (i in 0 until centers.lastIndex) {
                    val p0 = centers[i]
                    val p1 = centers[i + 1]
                    val midY = (p0.y + p1.y) / 2f
                    val side = if (i % 2 == 0) 1f else -1f
                    val dx = size.width * 0.18f * side

                    val c1 = Offset(p0.x + dx, midY)
                    val c2 = Offset(p1.x - dx, midY)
                    path.cubicTo(c1.x, c1.y, c2.x, c2.y, p1.x, p1.y)
                }
                drawPath(path, Aqua, style = Stroke(width = 16f, cap = StrokeCap.Round))
            }
        }

        // Botones
        Column(
            Modifier.matchParentSize().padding(vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            modules.forEachIndexed { index, module ->
                val alignment = rowAlignments.getOrElse(index) { Arrangement.Center }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                    NodeCard(
                        state = module.state,
                        onMeasuredCenter = { center -> registerCenter(index, center) },
                        onClick = { onTapModule(module.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NodeCard(
    state: ModuleState,
    onMeasuredCenter: (Offset) -> Unit,
    onClick: () -> Unit
) {
    val backgroundColor = if (state == ModuleState.LOCKED) LockedFill else ButtonFillLight
    val borderColor     = if (state == ModuleState.LOCKED) LockedBorder else ButtonBorder
    val iconTint        = if (state == ModuleState.LOCKED) Color.Gray else Navy
    // Permitir clic si NO está bloqueado (AVAILABLE o DONE)
    val isEnabled       = state != ModuleState.LOCKED

    Surface(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(enabled = isEnabled) { onClick() }
            .onGloballyPositioned { coords ->
                val size = coords.size
                val topLeft = coords.positionInRoot()
                onMeasuredCenter(Offset(topLeft.x + size.width / 2f, topLeft.y + size.height / 2f))
            },
        color = backgroundColor,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(4.dp, borderColor),
        tonalElevation = 0.dp,
        shadowElevation = if (state == ModuleState.LOCKED) 0.dp else 4.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (state) {
                ModuleState.AVAILABLE -> Icon(Icons.Default.PlayArrow, null, tint = iconTint, modifier = Modifier.size(48.dp))
                ModuleState.DONE      -> Icon(Icons.Default.Check, null, tint = iconTint, modifier = Modifier.size(44.dp))
                ModuleState.LOCKED    -> Icon(Icons.Default.Lock, null, tint = iconTint, modifier = Modifier.size(36.dp))
            }
        }
    }
}

/* ---------------- HOME PAGE ---------------- */

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onStartQuiz: (moduleId: Int) -> Unit = {}
) {
    val scroll = rememberScrollState()

    // Lista de módulos
    val modules = remember {
        mutableStateListOf(
            Module(1, ModuleState.AVAILABLE),
            Module(2, ModuleState.LOCKED),
            Module(3, ModuleState.LOCKED),
            Module(4, ModuleState.LOCKED),
            Module(5, ModuleState.LOCKED),
        )
    }

    // Progreso automático
    val doneCount by remember(modules.toList()) { derivedStateOf { modules.count { it.state == ModuleState.DONE } } }
    val progressPct by remember(doneCount) { derivedStateOf { if (modules.isEmpty()) 0 else (doneCount * 100f / modules.size).roundToInt() } }

    fun handleTapModule(id: Int) {
        // 1. Lanzar el quiz
        onStartQuiz(id)

        // 2. Lógica de desbloqueo INMEDIATO al presionar
        val idx = modules.indexOfFirst { it.id == id }
        if (idx != -1) {
            // Marcar actual como DONE
            if (modules[idx].state == ModuleState.AVAILABLE) {
                modules[idx] = modules[idx].copy(state = ModuleState.DONE)

                // Desbloquear el siguiente si existe
                if (idx + 1 < modules.size) {
                    modules[idx + 1] = modules[idx + 1].copy(state = ModuleState.AVAILABLE)
                }
            }
        }
    }

    Scaffold(
        containerColor = PageBg,
        topBar = { HomeTopBar() }
    ) { inner ->
        Column(
            modifier.fillMaxSize().padding(inner).verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(8.dp))
            CourseBanner(title = "Introducción a las Matemáticas", progressPct = progressPct)
            Spacer(Modifier.height(8.dp))
            RoadMapSection(
                modules = modules,
                onTapModule = ::handleTapModule,
                modifier = Modifier.height(660.dp)
            )
            Spacer(Modifier.height(96.dp))
        }
    }
}

/* ---------------- BANNER Y DONA ---------------- */

@Composable
private fun CourseBanner(title: String, progressPct: Int) {
    Surface(
        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = CardBg,
        shadowElevation = 2.dp
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, modifier = Modifier.weight(1f), color = Navy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
            ProgressDonut(progressPct)
        }
    }
}

@Composable
private fun ProgressDonut(progressPct: Int) {
    Box(Modifier.size(60.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            drawArc(Color(0xFFE6E9F2), -90f, 360f, false, style = Stroke(12f, cap = StrokeCap.Round))
            drawArc(Aqua, -90f, (progressPct / 100f) * 360f, false, style = Stroke(12f, cap = StrokeCap.Round))
        }
        Text("$progressPct%", color = Navy, fontWeight = FontWeight.Bold)
    }
}