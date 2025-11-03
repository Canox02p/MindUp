package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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

// =================== COLORES ===================
private val PageBg   = Color(0xFFEAF2FF)   // Fondo azul claro
private val CardBg   = Color.White
private val Navy     = Color(0xFF1E2746)   // Texto principal
private val Muted    = Color(0xFF7E8CA0)   // Texto secundario
private val Primary  = Color(0xFF2F6BFF)   // Azul acento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    userName: String = "Héctor",
    streakDays: Int = 6,
    plansCount: Int = 3,
    streaksCount: Int = 8,
    cardsCount: Int = 82,
    onEdit: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = PageBg,
        topBar = {
            TopAppBar(
                title = {
                    Text("MindUp", color = Navy, fontWeight = FontWeight.ExtraBold)
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 8.dp)
                    )
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            // ---------------- PERFIL ----------------
            Surface(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                color = CardBg,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Perfil", color = Primary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    Surface(
                        color = Color(0xFFF7F9FE),
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = 0.dp
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

            // ---------------- RESUMEN ----------------
            SectionHeader("Resumen")
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallStatCard(title = "Rachas", value = streaksCount.toString(), modifier = Modifier.weight(1f))
                SmallStatCard(title = "Planes", value = plansCount.toString(), modifier = Modifier.weight(1f))
                SmallStatCard(title = "Fichas", value = cardsCount.toString(), modifier = Modifier.weight(1f))
            }

            // ---------------- INSIGNIAS ----------------
            SectionHeader("Insignias", trailingArrow = true)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BadgeCard("Enero",  modifier = Modifier.weight(1f))
                BadgeCard("Febrero",modifier = Modifier.weight(1f))
                BadgeCard("Marzo",  modifier = Modifier.weight(1f))
            }

            // ---------------- LOGROS ----------------
            SectionHeader("Logros", trailingArrow = true)
            Spacer(Modifier.height(12.dp))

            // ---------------- BOTÓN CERRAR SESIÓN ----------------
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

    // ---------------- DIÁLOGO DE CONFIRMACIÓN ----------------
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

// ================== COMPONENTES REUTILIZABLES ==================

@Composable
private fun SectionHeader(text: String, trailingArrow: Boolean = false) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .fillMaxWidth(),
        color = CardBg,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 0.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = Primary, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            if (trailingArrow) Text("→", color = Muted, fontSize = 18.sp)
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
private fun BadgeCard(
    month: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(72.dp),
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
                HorizontalDivider(color = Color(0xFFE7ECF5))
            }
        }
    }
}
