package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private val PageBg = Color(0xFFF4F7FB)
private val CardBg = Color.White
private val Navy = Color(0xFF1B1F23)
private val Muted = Color(0xFF7E8CA0)
private val Primary = Color(0xFF1EC6D7)

@Composable
fun QuizPage(
    modifier: Modifier = Modifier,
    onConfirmHome: () -> Unit = {}
) {
    var selected by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = PageBg
    ) { inner ->
        Column(
            modifier = modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ===== Encabezado de progreso =====
            Text("Pregunta 1/5", color = Navy, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LinearProgressIndicator(
                progress = 0.2f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Primary,
                trackColor = Color(0xFFE7ECF5)
            )
            Text(
                "Nivel 1: Fundamentos del Desarrollo Web",
                color = Navy,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                "¿Qué lenguaje es responsable de la **estructura** y el contenido semántico de una página web?",
                color = Muted,
                fontSize = 14.sp
            )

            // ===== Opciones =====
            val options = listOf("A) JavaScript", "B) CSS", "C) HTML", "D) Python")

            options.forEachIndexed { index, text ->
                val isSelected = selected == index
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = CardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = if (isSelected)
                        BorderStroke(2.dp, Primary)
                    else BorderStroke(1.dp, Color(0xFFE7ECF5)),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    onClick = { selected = index }
                ) {
                    Text(
                        text,
                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = Navy
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ===== Pista =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(
                        id = android.R.drawable.ic_menu_info_details
                    ),
                    contentDescription = null,
                    tint = Muted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Pista", color = Muted, fontSize = 13.sp)
            }

            Spacer(Modifier.height(16.dp))

            // ===== Botón CONFIRMAR =====
            Button(
                onClick = { onConfirmHome() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text("CONFIRMAR", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
