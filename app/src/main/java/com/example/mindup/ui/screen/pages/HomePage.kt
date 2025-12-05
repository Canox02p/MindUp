package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val PageBg = Color(0xFFF6F9FF)
private val CardBg = Color.White
private val Navy   = Color(0xFF22264C)
private val Aqua   = Color(0xFF15D6DB)

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
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            color = CardBg,
            shape = RoundedCornerShape(50),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF4D4D),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(hearts, color = Navy, style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = Navy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(coins, color = Navy, style = MaterialTheme.typography.bodyMedium)
                }
                Surface(
                    color = Color(0xFFFFE0B2).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Racha: $streakDays Días",
                            color = Color(0xFFFF5722),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
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
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    tint = Navy,
                    modifier = Modifier.size(44.dp).padding(10.dp)
                )
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
            .onGloballyPositioned { coords ->
                containerOffsetInRoot = coords.positionInRoot()
            }
    ) {
        Canvas(
            Modifier
                .matchParentSize()
                .height(640.dp)
        ) {
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
                drawPath(
                    path = path,
                    color = Aqua,
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )
            }
        }

        Column(
            Modifier
                .matchParentSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            modules.forEachIndexed { index, module ->
                val alignment = rowAlignments.getOrElse(index) { Arrangement.Center }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                    NodeCard(
                        state = module.state,
                        onMeasuredCenter = { center -> registerCenter(index, center) },
                        onClick = { if (module.state == ModuleState.AVAILABLE) onTapModule(module.id) }
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
    Surface(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(enabled = state == ModuleState.AVAILABLE) { onClick() }
            .onGloballyPositioned { coords ->
                val size = coords.size
                val topLeft = coords.positionInRoot()
                val center = Offset(
                    x = topLeft.x + size.width / 2f,
                    y = topLeft.y + size.height / 2f
                )
                onMeasuredCenter(center)
            },
        color = CardBg,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        shadowElevation = 6.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (state) {
                ModuleState.AVAILABLE -> Icon(Icons.Default.PlayArrow, null, tint = Navy, modifier = Modifier.size(44.dp))
                ModuleState.DONE      -> Icon(Icons.Default.Bolt, null, tint = Navy, modifier = Modifier.size(40.dp))
                ModuleState.LOCKED    -> Icon(Icons.Default.Lock, null, tint = Navy, modifier = Modifier.size(36.dp))
            }
        }
    }
}

/* ---------------- HOME PAGE ---------------- */

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onStartQuiz: (moduleId: Int) -> Unit = {},
    completedModuleId: Int? = null,               // <- id que llega al volver del quiz
    onCompletedConsumed: () -> Unit = {}          // <- para limpiar el id una vez aplicado
) {
    val scroll = rememberScrollState()

    // Estado de módulos (persistente mientras viva esta composición)
    val modules = remember {
        mutableStateListOf(
            Module(1, ModuleState.AVAILABLE),
            Module(2, ModuleState.LOCKED),
            Module(3, ModuleState.LOCKED),
            Module(4, ModuleState.LOCKED),
            Module(5, ModuleState.LOCKED),
        )
    }

    // Si regresaste del quiz con un módulo completado, aplícalo aquí.
    LaunchedEffect(completedModuleId) {
        val id = completedModuleId ?: return@LaunchedEffect
        val idx = modules.indexOfFirst { it.id == id }
        if (idx != -1) {
            if (modules[idx].state == ModuleState.AVAILABLE) {
                modules[idx] = modules[idx].copy(state = ModuleState.DONE)
                if (idx + 1 < modules.size && modules[idx + 1].state == ModuleState.LOCKED) {
                    modules[idx + 1] = modules[idx + 1].copy(state = ModuleState.AVAILABLE)
                }
            }
        }
        onCompletedConsumed()
    }

    // progreso dinámico — memorizado
    val doneCount by remember(modules) {
        derivedStateOf { modules.count { it.state == ModuleState.DONE } }
    }
    val progressPct by remember(doneCount, modules.size) {
        derivedStateOf {
            if (modules.isEmpty()) 0 else (doneCount * 100f / modules.size).roundToInt()
        }
    }

    fun handleTapModule(id: Int) {
        // Ya NO marcamos como DONE aquí
        onStartQuiz(id)
    }

    Scaffold(
        containerColor = PageBg,
        topBar = { HomeTopBar() }
    ) { inner ->
        Column(
            modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(8.dp))
            PracticeCard(
                onPracticeClick = { handleTapModule(1) }
            )

            Spacer(Modifier.height(8.dp))

            CourseBanner(
                title = "Introducción a las Matemáticas",
                progressPct = progressPct
            )

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

/* ---------------- BANNER Y PROGRESO ---------------- */

@Composable
private fun CourseBanner(title: String, progressPct: Int) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = CardBg,
        shadowElevation = 2.dp
    ) {
        Row(
            Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                modifier = Modifier.weight(1f),
                color = Navy,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
            )
            ProgressDonut(progressPct)
        }
    }
}

@Composable
private fun ProgressDonut(progressPct: Int) {
    Box(Modifier.size(60.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0xFFE6E9F2),
                startAngle = -90f, sweepAngle = 360f, useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Aqua,
                startAngle = -90f, sweepAngle = (progressPct / 100f) * 360f, useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }
        Text("$progressPct%", color = Navy, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

/* ---------------- CARD PRÁCTICA ---------------- */

@Composable
fun PracticeCard(
    title: String = "Práctica para: Examen Final de Matemáticas",
    subtitle: String = "¡Tienes 20 preguntas pendientes para hoy!",
    onPracticeClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFE6FAFB),
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = Navy,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
            )
            Text(
                text = subtitle,
                color = Navy.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = onPracticeClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Aqua,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(42.dp)
            ) {
                Text("IR A PRACTICAR")
            }
        }
    }
}
