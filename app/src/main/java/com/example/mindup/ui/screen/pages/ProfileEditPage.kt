package com.example.mindup.ui.screen.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mindup.ui.screen.main.BicolorTitle
import com.example.mindup.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditPage(
    viewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val nameFlow   by viewModel.name.collectAsState(initial = "")
    val emailFlow  by viewModel.email.collectAsState(initial = "")
    val phoneFlow  by viewModel.phone.collectAsState(initial = "")
    val bioFlow    by viewModel.bio.collectAsState(initial = "")

    var name  by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var bio   by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(nameFlow, emailFlow, phoneFlow, bioFlow) {
        if (name.isEmpty() && email.isEmpty() && phone.isEmpty() && bio.isEmpty()) {
            name = nameFlow
            email = emailFlow
            phone = phoneFlow
            bio = bioFlow
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // 👇 Usamos el mismo estilo bonito del header principal
                    BicolorTitle(
                        text = "Editar perfil",
                        size = 24,
                        weight = FontWeight.W600
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biografía") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.save(
                        name = name.trim(),
                        email = email.trim(),
                        phone = phone.trim(),
                        bio = bio.trim()
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
