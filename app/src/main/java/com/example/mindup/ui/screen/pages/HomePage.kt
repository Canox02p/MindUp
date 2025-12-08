package com.example.mindup.ui.screen.pages

import androidx.compose.animation.AnimatedVisibility
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
import com.example.mindup.data.model.Materia
import com.example.mindup.data.prefs.UserPrefs
import com.example.mindup.ui.components.MateriaMenuList
import kotlinx.coroutines.launch

private val PageBg = Color(0xFFF6F9FF)
private val CardBg = Color.White
private val Navy   = Color(0xFF22264C)
private val Aqua   = Color(0xFF03A9F4)

private val ButtonFillLight = Color(0xFFE1F5FE)
private val ButtonBorder    = Color(0xFF4FC3F7)
private val LockedFill      = Color(0xFFF5F5F5)
private val LockedBorder    = Color(0xFFE0E0E0)

enum class ModuleState { LOCKED, AVAILABLE, DONE }
data class Module(val id: Int, val state: ModuleState, val title: String = "")

/* ---------------------------------------------------------
   TOP BAR MODIFICADO (CON MENÚ HAMBURGUESA Y TÍTULO DINÁMICO)
--------------------------------------------------------- */

@Composable
fun HomeTopBar(
    title: String,
    onMenuClick: () -> Unit,
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

                // ❤️ Vidas
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFFF4D4D),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(hearts, color = Navy)
                }

                // 💎 Diamantes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = Navy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(coins, color = Navy)
                }

                // 🔥 Racha
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
                        Text("Racha: $streakDays Días", color = Color(0xFFFF5722))
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // TITULO + MENÚ HAMBURGUESA (AQUÍ VA EL CAMBIO IMPORTANTE)
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CardBg,
                shadowElevation = 1.dp,
                modifier = Modifier.clickable { onMenuClick() }
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Navy,
                    modifier = Modifier.size(44.dp).padding(10.dp)
                )
            }

            Text(title, color = Navy, style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.size(44.dp))
        }
    }
}

/* ------------------------------------------------------------------
   AQUÍ EMPIEZA TU PANTALLA PRINCIPAL CON MAPA + MENÚ + TÍTULO DINÁMICO
------------------------------------------------------------------- */

@Composable
fun HomePage(
    materias: List<Materia>,
    prefs: UserPrefs,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onStartQuiz: (Int) -> Unit = {},
    completedModuleId: Int? = null,
    onCompletedConsumed: () -> Unit = {}
) {
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()

    // 🔶 Leer materia seleccionada desde DataStore
    val savedMateriaName by prefs.selectedMateriaName.collectAsState(initial = "Mis Cursos")

    // 🔶 Estado interno del título
    var title by remember { mutableStateOf(savedMateriaName) }

    // 🔶 Estado del menú
    var showMenu by remember { mutableStateOf(false) }

    // 🔶 Convertir materias reales en nodos del mapa
    val modules = remember(materias) {
        materias.map { materia ->
            Module(id = materia.id, state = ModuleState.AVAILABLE, title = materia.nombre)
        }
    }

    LaunchedEffect(completedModuleId) { onCompletedConsumed() }

    Scaffold(
        containerColor = PageBg,
        topBar = {
            HomeTopBar(
                title = title,
                onMenuClick = { showMenu = !showMenu }
            )
        }
    ) { inner ->

        /* ============ MENÚ DESPLEGABLE ============ */
        AnimatedVisibility(showMenu) {
            MateriaMenuList(
                materias = materias,
                onMateriaSelected = { materia ->

                    title = materia.nombre
                    showMenu = false

                    // Guardar selección
                    scope.launch {
                        prefs.saveSelectedMateria(materia.nombre, materia.id)
                    }
                }
            )
        }

        /* ============ CONTENIDO PRINCIPAL ============ */
        if (isLoading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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

                val progressPct = (modules.size * 10).coerceAtMost(100)

                CourseBanner(
                    title = "Continuar Aprendiendo",
                    progressPct = progressPct
                )

                Spacer(Modifier.height(8.dp))

                if (modules.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tienes materias inscritas aún.", color = Navy)
                    }
                } else {
                    RoadMapSection(
                        modules = modules,
                        onTapModule = { onStartQuiz(it) },
                        modifier = Modifier.height(660.dp)
                    )
                }

                Spacer(Modifier.height(96.dp))
            }
        }
    }
}

/* TODO LO DEMÁS SE QUEDA IGUAL (RoadMapSection, NodeCard, Banner, Donut)
   YA VIENE COMPLETO EN TU MISMO ARCHIVO ORIGINAL
*/

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
                        label = module.title,
                        onMeasuredCenter = { center -> registerCenter(index, center) },
                        onClick = { if (module.state == ModuleState.AVAILABLE || module.state == ModuleState.DONE) onTapModule(module.id) }
                    )
                }
            }
        }
    }
}

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
@Composable
fun MateriaMenuList(
    materias: List<Materia>,
    onMateriaSelected: (Materia) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = PageBg.copy(alpha = 0.98f),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                materias.forEachIndexed { index, materia ->

                    // Color de la tarjeta (puedes marcar una como seleccionada si quieres)
                    val isHighlighted = index == 2 // Ejemplo: la tercera en azul
                    val itemBg = if (isHighlighted) Color(0xFFE0F7FF) else CardBg

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onMateriaSelected(materia) },
                        color = itemBg,
                        shadowElevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            val icon = when {
                                materia.nombre.contains("Base", ignoreCase = true) ->
                                    Icons.Default.Home
                                materia.nombre.contains("Soft", ignoreCase = true) ->
                                    Icons.Default.Article
                                materia.nombre.contains("Diseño", ignoreCase = true) ->
                                    Icons.Default.TrackChanges
                                materia.nombre.contains("Compu", ignoreCase = true) ->
                                    Icons.Default.Notifications
                                else -> Icons.Default.School
                            }

                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Navy,
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = materia.nombre,
                                color = Navy,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
