package com.example.mindup.ui.screen.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

// --- Colores ---
private val Muted = Color(0xFF7E8CA0)
private val SoftBorder = Color(0xFFE7ECF5)
private val SoftTeal = Color(0xFFDBF6FF)
private val ContentBg = Color(0xFFEFF6FF) // Fondo azul muy clarito por defecto

// Colores específicos
private val BrandBlue = Color(0xFF0288D1)
private val CardBorder = Color(0xFFE1F5FE)
private val CardBg = Color(0xFFFFFFFF)
private val PressedCyan = Color(0xFF26C6DA) // Color Cyan intenso al presionar

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
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0),
        topBar = { /* sin AppBar */ }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(
                start = gutter, end = gutter,
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ======= 1. TARJETAS DE ESTADO =======
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusInfoCard(
                        iconRes = R.drawable.reloj,
                        value = "5 Días",
                        label = "Restantes",
                        modifier = Modifier.weight(1f)
                    )
                    StatusInfoCard(
                        iconRes = R.drawable.precision,
                        value = "80%",
                        label = "Precisión",
                        modifier = Modifier.weight(1f)
                    )
                    StatusInfoCard(
                        iconRes = R.drawable.calendario,
                        value = "2 Módulos",
                        label = "Pendientes",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ======= 2. BARRA DE PROGRESO =======
            item {
                Column {
                    ProgressBarLabeled(current = current, total = total, progress = progress)
                }
            }

            // ======= 3. RUTA DE ESTUDIO =======
            item { SectionHeader("Mi Ruta de Estudio") }

            item {
                RouteCard(
                    title = "HOY: Unidad 5 · Límites Avanzados",
                    bg = SoftTeal.copy(alpha = 1f),
                    border = MaterialTheme.colorScheme.primary.copy(alpha = .35f),
                    buttonText = "¡EMPEZAR!",
                    onClick = onStartToday,
                    primaryPressOnly = true
                )
            }

            item {
                RouteCard(
                    title = "MAÑANA: Repaso",
                    bg = Color(0xFFF7F9FC),
                    border = SoftBorder,
                    buttonText = "Ver detalle",
                    onClick = { },
                    forceWhiteButton = true
                )
            }

            // ======= 4. CONTENIDO PERSONAL =======
            item { SectionHeader("Mi Contenido Personal") }

            // Elemento 1: Banco de Preguntas
            item {
                SimpleIconCard(
                    iconRes = R.drawable.base,
                    title = "MI BANCO DE PREGUNTAS",
                    subtitle = "12 creadas",
                    bg = ContentBg,
                    onClick = onOpenBank
                )
            }

            // Elemento 2: Agregar nueva pregunta (Botón Interactivo)
            item {
                // Creamos la interacción
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                // Animamos el color: Si se presiona es Cyan, si no, es el fondo suave
                val animatedColor by animateColorAsState(
                    targetValue = if (isPressed) PressedCyan else ContentBg,
                    label = "colorAnim"
                )

                Surface(
                    onClick = onPremium, // Tu acción aquí
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = animatedColor, // Usamos el color animado
                    interactionSource = interactionSource,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono ic_add (círculo con más)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black // Icono negro
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Agrega una nueva pregunta",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black // Texto siempre negro
                            )
                        )
                    }
                }
            }

            // ======= 5. BOTÓN PREMIUM =======
            item {
                PremiumButton(
                    text = "Desbloquea Repaso Inteligente. ¡Hazte Premium!",
                    onClick = onPremium
                )
            }
        }
    }
}

/* --------------------------------------------------------- */
/* COMPONENTES REUTILIZABLES               */
/* --------------------------------------------------------- */

@Composable
fun StatusInfoCard(
    iconRes: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CardBorder),
        color = CardBg
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = BrandBlue
                    ),
                    lineHeight = 10.sp
                )
            }
        }
    }
}

@Composable
private fun PremiumButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFF31A5DC), shape = RoundedCornerShape(10.dp))
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
                .height(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF1F5F9))
        ) {
            Box(
                Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BrandBlue)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "$current/$total Preguntas (${(progress * 100).toInt()}%)",
            color = Muted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = Color(0xFF1E293B),
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(top = 8.dp)
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
                color = Color(0xFF334155),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Spacer(Modifier.width(10.dp))

            val interaction = remember { MutableInteractionSource() }
            val isPressed by interaction.collectIsPressedAsState()

            val container = when {
                primaryPressOnly && isPressed -> BrandBlue
                primaryPressOnly && !isPressed -> Color.White
                forceWhiteButton -> Color.White
                else -> BrandBlue
            }

            val content = when {
                primaryPressOnly && isPressed -> Color.White
                primaryPressOnly && !isPressed -> BrandBlue
                forceWhiteButton -> BrandBlue
                else -> buttonTint
            }

            Button(
                onClick = onClick,
                interactionSource = interaction,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = container,
                    contentColor = content
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Color(0xFF334155), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (subtitle.isNotEmpty()) {
                    Text(subtitle, color = Muted, fontSize = 12.sp)
                }
            }
        }
    }
}