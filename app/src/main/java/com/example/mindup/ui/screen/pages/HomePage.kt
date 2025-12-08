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
// 👇 IMPORTANTE: Importamos tu modelo de datos real
import com.example.mindup.data.model.Materia

// --- PALETA DE COLORES ---
private val PageBg = Color(0xFFF6F9FF)
private val CardBg = Color.White
private val Navy   = Color(0xFF22264C)
private val Aqua   = Color(0xFF03A9F4)

// Colores nuevos para los botones del mapa (Estilo solicitado)
private val ButtonFillLight = Color(0xFFE1F5FE) // Azul muy claro (fondo)
private val ButtonBorder    = Color(0xFF4FC3F7) // Azul cian (contorno)
private val LockedFill      = Color(0xFFF5F5F5) // Gris claro para niveles bloqueados
private val LockedBorder    = Color(0xFFE0E0E0) // Borde gris

enum class ModuleState { LOCKED, AVAILABLE, DONE }
// Mantenemos tu clase interna para que el mapa funcione igual
data class Module(val id: Int, val state: ModuleState, val title: String = "")

/* ---------------- TOP BAR ---------------- */

@Composable
fun HomeTopBar(
    title: String = "Mis Materias", // Título por defecto
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
                            fontWeight = FontWeight.Medium
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
                    modifier = Modifier
                        .size(44.dp)
                        .padding(10.dp)
                )
            }
            Text(title, color = Navy, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.size(44.dp))
        }
    }
}

/* ---------------- ROADMAP (SECCIÓN DEL MAPA) ---------------- */

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
        // Aseguramos que la lista tenga el tamaño suficiente
        while (centers.size <= index) centers.add(Offset.Zero)
        centers[index] = local
    }

    // Alineación en zig-zag
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
        // DIBUJO DE LA LÍNEA CONECTORA (CAMINO)
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

                    // Curvatura para el zig-zag
                    val side = if (i % 2 == 0) 1f else -1f
                    val dx = size.width * 0.18f * side

                    val c1 = Offset(p0.x + dx, midY)
                    val c2 = Offset(p1.x - dx, midY)

                    path.cubicTo(c1.x, c1.y, c2.x, c2.y, p1.x, p1.y)
                }
                // Dibujamos la línea azul
                drawPath(
                    path = path,
                    color = Aqua,
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )
            }
        }

        // COLUMNA DE BOTONES (NODOS)
        Column(
            Modifier
                .matchParentSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            modules.forEachIndexed { index, module ->
                val alignment = rowAlignments.getOrElse(index % rowAlignments.size) { Arrangement.Center }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = alignment) {
                    NodeCard(
                        state = module.state,
                        label = module.title, // Pasamos el nombre para (opcionalmente) mostrarlo
                        onMeasuredCenter = { center -> registerCenter(index, center) },
                        onClick = { if (module.state == ModuleState.AVAILABLE || module.state == ModuleState.DONE) onTapModule(module.id) }
                    )
                }
            }
        }
    }
}

/* ---------------- NODECARD (DISEÑO ACTUALIZADO) ---------------- */
@Composable
private fun NodeCard(
    state: ModuleState,
    label: String,
    onMeasuredCenter: (Offset) -> Unit,
    onClick: () -> Unit
) {
    // Definimos colores según el estado:
    val backgroundColor = if (state == ModuleState.LOCKED) LockedFill else ButtonFillLight
    val borderColor     = if (state == ModuleState.LOCKED) LockedBorder else ButtonBorder
    val iconTint        = if (state == ModuleState.LOCKED) Color.Gray else Navy

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(110.dp) // Tamaño grande del botón
                .clip(RoundedCornerShape(24.dp))
                .clickable(enabled = state != ModuleState.LOCKED) { onClick() }
                .onGloballyPositioned { coords ->
                    val size = coords.size
                    val topLeft = coords.positionInRoot()
                    val center = Offset(
                        x = topLeft.x + size.width / 2f,
                        y = topLeft.y + size.height / 2f
                    )
                    onMeasuredCenter(center)
                },
            color = backgroundColor,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(4.dp, borderColor),
            tonalElevation = 0.dp,
            shadowElevation = if (state == ModuleState.LOCKED) 0.dp else 4.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (state) {
                    ModuleState.AVAILABLE -> Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Jugar",
                        tint = iconTint,
                        modifier = Modifier.size(48.dp)
                    )
                    ModuleState.DONE -> Icon(
                        Icons.Default.Check,
                        contentDescription = "Completado",
                        tint = iconTint,
                        modifier = Modifier.size(44.dp)
                    )
                    ModuleState.LOCKED -> Icon(
                        Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = iconTint,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        // Opcional: Mostrar nombre debajo del nodo
        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = Navy, fontWeight = FontWeight.Bold)
        }
    }
}

/* ---------------- PANTALLA PRINCIPAL (HOME PAGE) ---------------- */

@Composable
fun HomePage(
    // 👇 1. AQUI ESTÁ EL CAMBIO: Recibimos datos reales
    materias: List<Materia> = emptyList(),
    isLoading: Boolean = false,

    modifier: Modifier = Modifier,
    onStartQuiz: (moduleId: Int) -> Unit = {},
    completedModuleId: Int? = null,
    onCompletedConsumed: () -> Unit = {}
) {
    val scroll = rememberScrollState()

    // 👇 2. TRANSFORMACIÓN: Convertimos tus Materias Reales en Botones del Mapa
    val modules = remember(materias) {
        if (materias.isEmpty()) {
            // Si no hay materias, lista vacía
            emptyList<Module>()
        } else {
            materias.mapIndexed { index, materia ->
                // Aquí decidimos el estado. Por ahora, todas DISPONIBLES para que las pruebes.
                // Podrías poner lógica tipo: if(index == 0) AVAILABLE else LOCKED
                Module(
                    id = materia.id,
                    state = ModuleState.AVAILABLE,
                    title = materia.nombre // Usamos el nombre real de la BD
                )
            }
        }
    }

    // Lógica para desbloquear niveles (La mantenemos igual por si la usas luego)
    // Nota: Como ahora viene de la BD, lo ideal sería que la BD te diga qué está desbloqueado.
    LaunchedEffect(completedModuleId) {
        onCompletedConsumed()
    }

    // Cálculo del porcentaje de progreso (Visual)
    val progressPct by remember(modules) {
        derivedStateOf {
            if (modules.isEmpty()) 0 else 10 // Valor de ejemplo
        }
    }

    fun handleTapModule(id: Int) {
        onStartQuiz(id)
    }

    Scaffold(
        containerColor = PageBg,
        topBar = { HomeTopBar(title = "Mis Cursos") } // Cambiamos el título
    ) { inner ->

        // 👇 3. ESTADO DE CARGA: Si está cargando, mostramos spinner
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Aqua)
            }
        } else {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(inner)
                    .verticalScroll(scroll)
            ) {

                Spacer(Modifier.height(8.dp))

                // Banner del curso
                CourseBanner(
                    title = if (modules.isNotEmpty()) "Continuar Aprendiendo" else "Empieza un curso",
                    progressPct = progressPct
                )

                Spacer(Modifier.height(8.dp))

                if (modules.isEmpty()) {
                    // Mensaje si la lista está vacía
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tienes materias inscritas aún.", color = Navy)
                    }
                } else {
                    // Sección del Mapa (¡TU DISEÑO INTACTO!)
                    // El mapa dibujará tus materias reales
                    RoadMapSection(
                        modules = modules,
                        onTapModule = ::handleTapModule,
                        modifier = Modifier.height(660.dp)
                    )
                }

                Spacer(Modifier.height(96.dp))
            }
        }
    }
}

/* ---------------- BANNER Y DONA DE PROGRESO ---------------- */

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
                fontWeight = FontWeight.ExtraBold
            )
            ProgressDonut(progressPct)
        }
    }
}

@Composable
private fun ProgressDonut(progressPct: Int) {
    Box(Modifier.size(60.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            // Fondo gris del círculo
            drawArc(
                color = Color(0xFFE6E9F2),
                startAngle = -90f, sweepAngle = 360f, useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
            // Progreso azul
            drawArc(
                color = Aqua,
                startAngle = -90f, sweepAngle = (progressPct / 100f) * 360f, useCenter = false,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }
        Text("$progressPct%", color = Navy, fontWeight = FontWeight.Bold)
    }
}