package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.R
import com.example.mindup.ui.components.ProfileMapComponent
import com.example.mindup.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    vm: ProfileViewModel,
    onEdit: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    // DataStore
    LaunchedEffect(Unit) { vm.name.collectLatest { name = it } }
    LaunchedEffect(Unit) { vm.email.collectLatest { email = it } }
    LaunchedEffect(Unit) { vm.phone.collectLatest { phone = it } }
    LaunchedEffect(Unit) { vm.bio.collectLatest { bio = it } }
    //lista de lenguajes
    val languages = listOf(
        "Kotlin", "Java", "JavaScript", "TypeScript", "Python",
        "C", "C++", "C#", "PHP", "Go", "Swift", "SQL", "Dart"
    )
    //marjenes
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // 1) Portada
        item {
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.portada),
                    contentDescription = "Portada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)// tamano de la imagen
                        .padding(top = 4.dp), // distancia de la notch
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                        .height(220.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painter = painterResource(R.drawable.foto),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // 2) Nombre y Email
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -15.dp) //separacion de la foto -
                    .padding(top = 4.dp) // separacion
            ) {
                Text(
                    name.ifBlank { "Tu nombre" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    email.ifBlank { "correo@ejemplo.com" },
                    fontSize = 13.sp
                )
            }
        }

        // 3) Botón "Editar perfil"
        item {
            Row(
                modifier = Modifier
                    .offset(y = -120.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 0.dp), //pocison de el margen izq
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = onEdit) { // estilo
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editar perfil")
                }
            }
        }
        // 4) Lenguajes
        item {
            Column(
                modifier = Modifier.offset(y = -80.dp) // ajsta la separacionde los bloques que se separan por defecto
            ) {
                Text(
                    "Lenguajes que conozco",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(languages) { label ->
                        SuggestionChip(
                            onClick = { /* opcional */ },
                            label = { Text(label) },
                            shape = RoundedCornerShape(24.dp),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            }
        }
        // 5) Biografía
        item {
            Column(
                modifier = Modifier.offset(y = -80.dp) // ajsta la separacionde los bloques que se separan por defecto
            ) {
                Text(
                    "Biografía",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(6.dp))
                Text(bio.ifBlank { "Sin biografía aún." })
            }
        }
        // 6) Mapa
        item {
            Column(
                modifier = Modifier.offset(y = -80.dp) // ajsta la separacionde los bloques que se separan por defecto
            ) {
                Spacer(Modifier.height(6.dp))
                Text("Ubicación", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    ProfileMapComponent(
                        modifier = Modifier.fillMaxSize(),
                        interactive = true
                    )
                }
            }
        }
    }
}