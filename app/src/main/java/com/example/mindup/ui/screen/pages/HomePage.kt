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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// ---- Paleta / modelos ----
private val PageBg = Color(0xFFF6F9FF)
private val CardBg = Color.White
private val Navy   = Color(0xFF22264C)
private val Aqua   = Color(0xFF15D6DB)

enum class ModuleState { LOCKED, AVAILABLE, DONE }
data class Module(val id: Int, val state: ModuleState)
// ==============================
// PARTE 1: Indicadores + título
// ==============================
@Composable
fun HomeTopBar(
    title: String = "Desarrollador Full-Stack",
    hearts: String = "5",
    coins: String = "140",
    energy: String = "0",
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(PageBg)
            .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TopPill(Icons.Default.Favorite, hearts, Color(0xFFFF6B6B), Modifier.weight(1f))
            TopPill(Icons.Default.MonetizationOn, coins, Color(0xFFFFC107), Modifier.weight(1f))
            TopPill(Icons.Default.WaterDrop, energy, Color(0xFF6F86FF), Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(shape = RoundedCornerShape(14.dp), color = CardBg, shadowElevation = 1.dp) {
                Icon(Icons.Default.Menu, null, tint = Navy, modifier = Modifier.size(44.dp).padding(10.dp))
            }
            Text(title, color = Navy, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.size(44.dp)) // placeholder derecha
        }
    }
}

@Composable
private fun TopPill(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, tint: Color, modifier: Modifier) {
    Surface(modifier = modifier.height(44.dp), color = CardBg, shape = RoundedCornerShape(14.dp), shadowElevation = 1.dp) {
        Row(
            Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = tint)
            Spacer(Modifier.width(8.dp))
            Text(value, color = Navy, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun RoadMapSection(
    modifier: Modifier = Modifier,
    onStartQuiz: (moduleId: Int) -> Unit = {}
) {
    // Estado de módulos (puedes moverlo a un ViewModel luego)
    var modules by remember {
        mutableStateOf(
            listOf(
                Module(1, ModuleState.AVAILABLE),
                Module(2, ModuleState.LOCKED),
                Module(3, ModuleState.LOCKED),
                Module(4, ModuleState.LOCKED),
                Module(5, ModuleState.LOCKED),
            )
        )
    }

    // Medidas de contenedor y centros de cada nodo (coordenadas locales al contenedor)
    var containerOffsetInRoot by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val centers = remember { mutableStateListOf<Offset>() } // se rellena dinámicamente

    // Helper para registrar centro de un nodo en orden
    fun registerCenter(index: Int, centerInRoot: Offset) {
        val local = centerInRoot - containerOffsetInRoot
        while (centers.size <= index) centers.add(Offset.Zero)
        centers[index] = local
    }

    fun tapNode(id: Int) {
        val idx = modules.indexOfFirst { it.id == id }
        if (idx == -1) return
        if (modules[idx].state != ModuleState.AVAILABLE) return

        onStartQuiz(id)

        val m = modules.toMutableList()
        m[idx] = m[idx].copy(state = ModuleState.DONE)
        if (idx + 1 < m.size && m[idx + 1].state == ModuleState.LOCKED) {
            m[idx + 1] = m[idx + 1].copy(state = ModuleState.AVAILABLE)
        }
        modules = m
    }

    val rowAlignments = listOf(
        Arrangement.Center,   // nodo 0
        Arrangement.End,      // nodo 1
        Arrangement.Center,   // nodo 2
        Arrangement.Start,    // nodo 3
        Arrangement.Center    // nodo 4
    )

    Box(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .onGloballyPositioned { coords ->
                containerOffsetInRoot = coords.positionInRoot()
                containerSize = coords.size
            }
    ) {
        Canvas(
            Modifier
                .matchParentSize()
                .height(640.dp) // alto del roadmap
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

        // --- 2.2 Column: NODOS INTERACTIVOS (miden y registran sus centros) ---
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
                        index = index,
                        state = module.state,
                        onMeasuredCenter = { center -> registerCenter(index, center) },
                        onClick = { tapNode(module.id) }
                    )
                }
            }
        }
    }
}

// Tarjeta del nodo: mide su centro y lo reporta
@Composable
private fun NodeCard(
    index: Int,
    state: ModuleState,
    onMeasuredCenter: (Offset) -> Unit,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
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
// ======================================
// PARTE 3: Scroll y armado de la pantalla
// ======================================
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onStartQuiz: (moduleId: Int) -> Unit = {}
) {
    val scroll = rememberScrollState()

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

            // Banda del curso + % (simple)
            val done = remember { mutableStateOf(0) }
            val total = 5
            CourseBanner(
                title = "Introducción al Desarrollo Web",
                progressPct = (done.value.toFloat() / total * 100).roundToInt()
            )

            Spacer(Modifier.height(8.dp))

            // Camino + Nodos
            RoadMapSection(
                modifier = Modifier.height(660.dp), // alto visible del bloque
                onStartQuiz = onStartQuiz
            )

            // Scroll extra abajo (tope para que no se corte)
            Spacer(Modifier.height(96.dp))
        }
    }
}

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
