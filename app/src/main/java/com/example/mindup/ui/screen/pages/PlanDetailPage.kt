package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

private val Muted = Color(0xFF7E8CA0)
private val SoftBorder = Color(0xFFE7ECF5)
private val SoftTeal = Color(0xFFDBF6FF)
private val ContentBg = Color(0xFFDBF6FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailPage(
    onStartToday: () -> Unit = {},
    onOpenBank: () -> Unit = {},
    onPremium: () -> Unit = {}
) {
    val gutter = 12.dp
    val progress by remember { mutableFloatStateOf(0.75f) }
    val total = 20
    val current = (progress * total).toInt()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        topBar = { /* sin AppBar */ }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(
                start = gutter, end = gutter,
                top = 0.dp, bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chip con reloj
                    AssistChip(
                        onClick = { },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("5 Días", fontSize = 12.sp)
                                Text("Restantes", fontSize = 10.sp)
                            }
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.reloj), // Asegúrate de tener el ícono de reloj en los recursos
                                contentDescription = "Reloj",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFEFF7FF),
                            labelColor = Color(0xFF167ABF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFD3E8FF))
                    )

                    // Chip con precisión
                    AssistChip(
                        onClick = { },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("80%", fontSize = 12.sp)
                                Text("Precisión", fontSize = 10.sp)
                            }
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.precision),
                                contentDescription = "Precisión",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFEFF7FF),
                            labelColor = Color(0xFF167ABF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFD3E8FF))
                    )

                    // Chip con calendario
                    AssistChip(
                        onClick = { },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("2 Módulos", fontSize = 12.sp)
                                Text("Pendientes", fontSize = 10.sp)
                            }
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.calendario),
                                contentDescription = "Calendario",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFEFF7FF),
                            labelColor = Color(0xFF167ABF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFD3E8FF))
                    )
                }
            }

            // ======= Progreso =======
            item {
                Column {
                    Spacer(Modifier.height(6.dp))
                    ProgressBarLabeled(current = current, total = total, progress = progress)
                }
            }

            // ======= Ruta de estudio =======
            item { SectionHeader("Mi Ruta de Estudio") }

            // ======= Tarjeta de hoy =======
            item {
                RouteCard(
                    title = "HOY: Unidad 5 · Límites Avanzados (25 min)",
                    bg = SoftTeal.copy(alpha = 1f),
                    border = MaterialTheme.colorScheme.primary.copy(alpha = .35f),
                    buttonText = "¡EMPEZAR!",
                    onClick = onStartToday,
                    primaryPressOnly = true
                )
            }

            // ======= Tarjeta de mañana =======
            item {
                RouteCard(
                    title = "MAÑANA: Repaso · Derivadas e Integrales (15 min)",
                    bg = Color(0xFFF7F9FC),
                    border = SoftBorder,
                    buttonText = "Ver detalle",
                    onClick = { },
                    forceWhiteButton = true
                )
            }

            // ======= Contenido personal =======
            item { SectionHeader("Mi Contenido Personal") }

            items(
                listOf(
                    Triple(R.drawable.base, "MI BANCO DE PREGUNTAS", "12 creadas"),
                    Triple(R.drawable.cohete, "Agrega una nueva pregunta", "")
                )
            ) { (iconRes, title, subtitle) ->
                val onClick = if (iconRes == R.drawable.base) onOpenBank else onPremium
                SimpleIconCard(iconRes, title, subtitle, ContentBg, onClick)
            }

            // ======= Nuevo botón Premium =======
            item {
                PremiumButton(
                    text = "Desbloquea Repaso Inteligente. ¡Hazte Premium!",
                    onClick = onPremium
                )
            }
        }
    }
}

/* ---------- Reusables ---------- */

@Composable
private fun PremiumButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0xFF31C2DC), shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.corona),
                contentDescription = "Icono Premium",
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun ProgressBarLabeled(current: Int, total: Int, progress: Float) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SoftBorder)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text("$current/$total Preguntas (${(progress * 100).toInt()}%)", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun RouteCard(
    title: String,
    bg: Color,
    border: Color,
    buttonText: String,
    onClick: () -> Unit,
    buttonTint: Color = Color.White,
    forceWhiteButton: Boolean = false,
    primaryPressOnly: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = bg,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, border)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(10.dp))

            val interaction = remember { MutableInteractionSource() }
            val isPressed by interaction.collectIsPressedAsState()

            val container = when {
                primaryPressOnly && isPressed -> MaterialTheme.colorScheme.primary
                primaryPressOnly && !isPressed -> Color.White
                forceWhiteButton -> Color.White
                else -> MaterialTheme.colorScheme.primary
            }

            val content = when {
                primaryPressOnly && isPressed -> Color.White
                primaryPressOnly && !isPressed -> MaterialTheme.colorScheme.primary
                forceWhiteButton -> MaterialTheme.colorScheme.primary
                else -> buttonTint
            }

            Button(
                onClick = onClick,
                interactionSource = interaction,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = container,
                    contentColor = content
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(buttonText, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleIconCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    bg: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bg,
        shadowElevation = 0.dp,
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
        }
    }
}
