package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.R
import com.example.mindup.ui.viewmodel.ProfileViewModel

/* ===== Paleta ===== */
private val Muted = Color(0xFF7E8CA0)
private val CardBg = Color.White
private val Soft = Color(0xFFF2F6FC)
private val SoftBorder = Color(0xFFE7ECF5)
private val TealPromo = Color(0xFF1EC6D7)
private val StreakBg = Color(0xFFFFE4D6)
private val StreakText = Color(0xFFB85B2A)

@Composable
fun ProfileView(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    precision: Int = 85,
    cursosActivos: Int = 3,
    puntos: Int = 140,
    streakDays: Int = 6,
    onEdit: () -> Unit = {},
    onLogout: () -> Unit = {},
    onHelp: () -> Unit = {},
    onBadges: () -> Unit = {},
    onContrib: () -> Unit = {}
) {

    val name by viewModel.name.collectAsState(initial = "Héctor")
    var confirm by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .background(Soft)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        /* =================== ENCABEZADO =================== */
        Surface(
            color = CardBg,
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 2.dp
        ) {
            Box(Modifier.fillMaxWidth()) {

                /* ✅ Contenido del header sin espacio extra */
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE9EFFB))
                        )

                        Spacer(Modifier.width(10.dp))

                        Column(Modifier.weight(1f)) {

                            Text(
                                text = "¡Hola, $name!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = StreakBg
                            ) {
                                Text(
                                    "🔥  Racha: $streakDays Días",
                                    color = StreakText,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 5.dp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    SimplePill(
                        leading = {
                            Icon(
                                Icons.Filled.Star,
                                null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        text = "Logro: Estudiante Constante"
                    )
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(34.dp)
                        .align(Alignment.TopEnd)
                        .background(Color(0xFFF2F6FC), CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Editar perfil",
                        tint = Color(0xFF4A5668),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        /* =================== MÉTRICAS =================== */
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard("Precisión Global", "$precision%", modifier = Modifier.weight(1f))
            MetricCard("Cursos Activos", "$cursosActivos", modifier = Modifier.weight(1f))
            MetricCard("Puntos Totales", "$puntos", modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(10.dp))

        /* =================== ACCESOS =================== */
        ActionRow(text = "Logros e Insignias", onClick = onBadges)
        Spacer(Modifier.height(12.dp))
        ActionRow(text = "Mis Contribuciones", onClick = onContrib)
        Spacer(Modifier.height(10.dp))

        /* =================== PROMO =================== */
        Surface(
            color = TealPromo,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 0.dp
        ) {
            Column(Modifier.fillMaxWidth().padding(14.dp)) {

                Text(
                    "Desbloquea vidas infinitas y ventajas exclusivas con MindUp Premium",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onHelp,
                    border = ButtonDefaults.outlinedButtonBorder,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text("Actualizar ahora", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        /* =================== CONFIGURACIÓN =================== */
        RowItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Help,
                    null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            text = "Configuración y ayuda",
            onClick = onHelp
        )
        Spacer(Modifier.height(10.dp))
        RowItem(
            icon = {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFD14343))
            },
            text = "Cerrar sesión",
            onClick = { confirm = true }
        )

        Spacer(Modifier.height(12.dp))
    }

    /* =================== ALERTA SALIR =================== */
    if (confirm) {
        AlertDialog(
            onDismissRequest = { confirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Seguro que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = { confirm = false; onLogout() }) {
                    Text("Sí, salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/* =================== COMPONENTES REUTILIZABLES =================== */

@Composable
private fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(86.dp),
        color = CardBg,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Muted, fontSize = 12.sp)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun SimplePill(leading: @Composable () -> Unit, text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, SoftBorder)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leading()
            Spacer(Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ActionRow(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFE9EFF6),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().clickable { onClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            Text("›", color = Muted, fontSize = 22.sp)
        }
    }
}

@Composable
private fun RowItem(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            Modifier.fillMaxWidth().clickable { onClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(Modifier.width(10.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}
