package com.example.mindup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.data.model.Materia
import com.example.mindup.ui.theme.MindUpNavy

@Composable
fun MateriaMenuList(
    materias: List<Materia>,
    onMateriaSelected: (Materia) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        materias.forEach { materia ->

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onMateriaSelected(materia) },
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ----- Nombre de la materia -----
                    Text(
                        text = materia.nombre,
                        fontSize = 16.sp,
                        color = MindUpNavy,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
