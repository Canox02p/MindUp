package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Muted = Color(0xFF7E8CA0)
private val SoftBorder = Color(0xFFE7ECF5)

data class Reminder(
    val id: String,
    val text: String,
    val minutes: Int = 5
)

@Composable
fun NotificationPage(
    modifier: Modifier = Modifier,
    reminders: List<Reminder> = listOf(
        Reminder("1", "Revisa tus fichas de Geometría", 5),
        Reminder("2", "Revisa tus fichas de Biología", 15),
        Reminder("3", "Revisa tus fichas de Física", 25),
        Reminder("4", "Revisa tus fichas de Química", 35),
    ),
    onConfigure: (Reminder) -> Unit = {},
    onSnooze: (Reminder) -> Unit = {}
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val GUTTER = 12.dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = inner.calculateStartPadding(LayoutDirection.Ltr),
                        end   = inner.calculateEndPadding(LayoutDirection.Ltr),
                        bottom= inner.calculateBottomPadding()
                    )
                )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = GUTTER, end = GUTTER, top = 10.dp, bottom = 6.dp)
            ) {

            }

            // --- Lista de recordatorios ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = GUTTER, vertical = 10.dp),
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
}

@Composable
private fun ReminderCard(
    reminder: Reminder,
    onSnooze: () -> Unit,
    onConfigure: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = reminder.text,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            // Línea de tiempo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "${reminder.minutes} min",
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
                    colors = ButtonDefaults.outlinedButtonColors(),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(SoftBorder)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Posponer") }

                Button(
                    onClick = onConfigure,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Configurar", fontWeight = FontWeight.SemiBold) }
            }
        }
    }
}
