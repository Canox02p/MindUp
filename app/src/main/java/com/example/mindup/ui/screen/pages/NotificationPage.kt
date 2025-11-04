package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Paleta rápida para consistencia
private val PageBg = Color(0xFFF4F7FB)
private val CardBg = Color.White
private val Navy   = Color(0xFF1E2746)
private val Muted  = Color(0xFF7E8CA0)
private val PrimaryBlue = Color(0xFF2F6BFF)
private val SoftBorder = Color(0xFFE7ECF5)

data class Reminder(
    val id: String,
    val title: String = "Recordatorios",
    val text: String,
    val minutes: Int = 5
)

@Composable
fun NotificationPage(
    modifier: Modifier = Modifier,
    reminders: List<Reminder> = listOf(
        Reminder("1", text = "Revisa tus fichas de Geometría", minutes = 5),
        Reminder("2", text = "Revisa tus fichas de Geometría", minutes = 5),
    ),
    onConfigure: (Reminder) -> Unit = {},
    onSnooze: (Reminder) -> Unit = {}
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = PageBg,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = inner.calculateStartPadding(LayoutDirection.Ltr),
                        end   = inner.calculateEndPadding(LayoutDirection.Ltr),
                        bottom= inner.calculateBottomPadding()
                    )
                ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reminders, key = { it.id }) { r ->
                ReminderCard(
                    reminder = r,
                    onSnooze = {
                        onSnooze(r)
                        scope.launch { snackbar.showSnackbar("Pospuesto 10 minutos") }
                    },
                    onConfigure = {
                        onConfigure(r)
                        scope.launch { snackbar.showSnackbar("Abrir configuración de recordatorios…") }
                    }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

/* TopBar opcional (no usado para evitar duplicado). Déjalo si luego lo quieres reutilizar. */
@Composable
private fun AlertsTopBar() {
    Surface(color = Color(0xFFE9F1FF)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MindUp", color = Navy, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Box(
                Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AccountCircle, null, tint = Muted, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: Reminder,
    onSnooze: () -> Unit,
    onConfigure: () -> Unit
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Título
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    reminder.title,
                    color = Navy,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }

            Spacer(Modifier.height(6.dp))

            // Línea de “SRS / tiempo”
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "${reminder.text} (${reminder.minutes} min)",
                    color = Muted,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            // Acciones
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSnooze,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(SoftBorder)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Posponer")
                }
                Button(
                    onClick = onConfigure,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Configurar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
