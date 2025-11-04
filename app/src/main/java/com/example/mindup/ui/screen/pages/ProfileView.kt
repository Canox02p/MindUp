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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.R

// ======= PALETA =======
private val PageBg   = Color(0xFFEAF2FF)   // Fondo azul claro
private val CardBg   = Color.White
private val Navy     = Color(0xFF1B1F23)   // Texto principal (más oscuro)
private val Muted    = Color(0xFF7E8CA0)   // Texto secundario
private val Primary  = Color(0xFF2B9FD6)   // Azul acento MindUp
private val PrimaryDark = Color(0xFF1E86BD)

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    userName: String = "Héctor",
    streakDays: Int = 6,
    plansCount: Int = 3,
    streaksCount: Int = 8,
    cardsCount: Int = 82,
    onEdit: () -> Unit = {},
    onLogout: () -> Unit = {},
    onInsigniasClick: () -> Unit = {},
    onLogrosClick: () -> Unit = {}
) {
    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = PageBg,
        topBar = {
            // Header tipo “tarjeta” como en la captura
            HeaderBar()
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
        ) {

            // ======== PERFIL ========
            ElevatedCard(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    SectionTitle("Perfil")

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = Color(0xFFF7F9FE),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_foreground),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE9EFFB))
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(userName, color = Navy, fontWeight = FontWeight.SemiBold)
                                Text("Racha: $streakDays días 🔥", color = Muted, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Editar")
                    }
                }
            }

            // ======== RESUMEN ========
            SectionHeaderRow(
                text = "Resumen",
                showChevron = false
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallStatCard("Rachas", streaksCount.toString(), Modifier.weight(1f))
                SmallStatCard("Planes", plansCount.toString(), Modifier.weight(1f))
                SmallStatCard("Fichas", cardsCount.toString(), Modifier.weight(1f))
            }

            // ======== INSIGNIAS ========
            SectionHeaderRow(
                text = "Insignias",
                showChevron = true,
                onClick = onInsigniasClick
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BadgeCard("Enero",  Modifier.weight(1f))
                BadgeCard("Febrero",Modifier.weight(1f))
                BadgeCard("Marzo",  Modifier.weight(1f))
            }

            // ======== LOGROS ========
            SectionHeaderRow(
                text = "Logros",
                showChevron = true,
                onClick = onLogrosClick
            )
            Spacer(Modifier.height(12.dp))

            // ======== CERRAR SESIÓN ========
            Button(
                onClick = { showConfirm = true },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF5350),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Seguro que deseas salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onLogout()
                }) { Text("Sí, salir") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}

// ======= Header “MindUp” tipo tarjeta =======
@Composable
private fun HeaderBar() {
    // Altura pequeña como en la captura
    Surface(color = PageBg) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                // Franja superior con un tinte azul muy suave
                Box(
                    Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFEFF7FF), Color.White)
                            )
                        )
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Título “MindUp” con letras bicolor
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = Navy,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.W700
                                    )
                                ) { append("M") }
                                withStyle(
                                    SpanStyle(
                                        color = Primary,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.W700
                                    )
                                ) { append("indUp") }
                            }
                        )
                        Spacer(Modifier.weight(1f))
                        // Logo a la derecha
                        Image(
                            painter = painterResource(
                                // usa tu recurso: cambia por tu id si es otro
                                id = R.drawable.ic_logo_mindup
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

// ======= Componentes reutilizables =======
@Composable
private fun SectionTitle(text: String) {
    Text(text, color = Primary, fontWeight = FontWeight.Bold)
}

@Composable
private fun SectionHeaderRow(
    text: String,
    showChevron: Boolean,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = showChevron) { onClick() },
        color = CardBg,
        shadowElevation = 0.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                color = Primary,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
            if (showChevron) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Muted
                )
            }
        }
    }
}

@Composable
private fun SmallStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(72.dp),
        color = CardBg,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, color = Navy, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(Modifier.height(2.dp))
            Text(title, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun BadgeCard(month: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(72.dp),
        color = CardBg,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⭐", fontSize = 22.sp)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(month, color = Navy, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text("Mes", color = Muted, fontSize = 11.sp)
            }
        }
    }
}
