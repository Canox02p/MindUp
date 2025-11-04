package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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

// ======= Paleta (misma que UI) =======
private val PageBg   = Color(0xFFEAF2FF)
private val CardBg   = Color.White
private val Navy     = Color(0xFF1B1F23)
private val Muted    = Color(0xFF7E8CA0)
private val Primary  = Color(0xFF2B9FD6)

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(8.dp))

        // ======== PERFIL ========
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = CardBg,   // 🔁 Color.White
                contentColor = Navy
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Perfil", color = Primary, fontWeight = FontWeight.Bold)
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Primary
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE7ECF5))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Primary)
                    Spacer(Modifier.width(6.dp))
                    Text("Editar")
                }
            }
        }

        // ======== RESUMEN ========
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

        // ======== INSIGNIAS ========
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

        // ======== LOGROS ========
        SectionHeader("Logros", trailingArrow = true)
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

// ======= Reusables =======

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
                HorizontalDivider(color = Color(0xFFE7ECF5))
            }
        }
    }
}
