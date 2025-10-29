package com.example.mindup.ui.screen.recover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mindup.data.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun RecoverScreen(
    repo: UserRepository,
    onRecovered: () -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var err by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Recuperar cuenta", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo registrado") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Nueva contraseña (mín. 6)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            err?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    loading = true
                    err = null
                    scope.launch {
                        val res = repo.recover(email, pass)
                        res.onSuccess {
                            loading = false
                            onRecovered()
                        }.onFailure {
                            loading = false
                            err = it.message
                        }
                    }
                },
                enabled = !loading && email.contains("@") && pass.length >= 6,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Actualizando…" else "Actualizar")
            }

            TextButton(onClick = onBack) { Text("Volver") }
        }
    }
}
