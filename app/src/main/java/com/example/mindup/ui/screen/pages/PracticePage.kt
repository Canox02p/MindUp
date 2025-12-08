package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Muted = Color(0xFF7E8CA0)
private val ChipBg = Color(0xFFEFF7FF)

@Composable
fun PracticePage(
    onQuickReview: () -> Unit = {},
    onDomainQuiz: () -> Unit = {},
    onChallenges: () -> Unit = {},
    onLeaderboard: () -> Unit = {},
    onFreePractice: () -> Unit = {}
) {
    val gutter = 12.dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = gutter)
        ) {
            Spacer(Modifier.height(6.dp))

            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Default.Bolt,
                    iconBg = MaterialTheme.colorScheme.primary.copy(alpha = .12f),
                    title = "Repaso Rápido",
                    subtitle = "Diario",
                    onClick = onQuickReview,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    icon = Icons.Default.Timer,
                    iconBg = MaterialTheme.colorScheme.primary.copy(alpha = .12f),
                    title = "Quiz de Dominio",
                    subtitle = "(Simulacro)",
                    onClick = onDomainQuiz,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            SectionHeader("Desafíos y Comunidad")

            OptionRow(
                leading = {
                    CircleChip { Text("VS", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold) }
                },
                title = "Desafíos contra amigos",
                onClick = onChallenges
            )

            OptionRow(
                leading = {
                    CircleChip { Icon(Icons.Default.Leaderboard, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                },
                title = "Tabla de Clasificación Semanal",
                onClick = onLeaderboard
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onFreePractice,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Comenzar Práctica Libre", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column {
                Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionRow(
    leading: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leading()
            Spacer(Modifier.width(10.dp))
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        }
    }
    Spacer(Modifier.height(10.dp))
}
@Composable
private fun CircleChip(content: @Composable RowScope.() -> Unit) {
    Surface(
        shape = CircleShape,
        color = ChipBg,
        shadowElevation = 0.dp
    ) {
        Row(
            Modifier
                .height(34.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
