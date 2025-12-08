package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mindup.data.model.Materia
import com.example.mindup.ui.theme.MindUpCardBg
import com.example.mindup.ui.theme.MindUpNavy
import com.example.mindup.ui.theme.MindUpPageBg

@Composable
fun SelectMateriaPage(
    materias: List<Materia>,
    onMateriaSelected: (Materia) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MindUpPageBg)
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp) // ⭐ Baja todo el contenido
    ) {

        // ---------- TÍTULO ----------
        Text(
            text = "¿Con qué quieres empezar?",
            color = MindUpNavy,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Spacer(Modifier.height(20.dp))

        // ---------- LISTA DE MATERIAS ----------
        materias.forEach { materia ->

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MindUpCardBg,
                shadowElevation = 3.dp,
                shape = RoundedCornerShape(20.dp)
            ) {

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ---------- IMAGEN ----------
                    AsyncImage(
                        model = materia.imagen ?: materia.iconoLocal,
                        contentDescription = materia.nombre,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDCECF8))
                    )

                    Spacer(Modifier.width(12.dp))

                    // ---------- INFORMACIÓN ----------
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = materia.nombre,
                            color = MindUpNavy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = materia.descripcion ?: "Sin descripción",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        // ---------- BOTÓN ----------
                        Button(
                            onClick = { onMateriaSelected(materia) },
                            modifier = Modifier
                                .height(38.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Comenzar", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
