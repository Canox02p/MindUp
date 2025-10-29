package com.example.mindup.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private val SkyBlue = Color(0xFF87CEEB) // azul cielo

@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onRegisteredOk: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val ui by vm.ui.collectAsState()
    var showPass by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBlue.copy(alpha = 0.15f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall, color = SkyBlue)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = ui.alias,
                onValueChange = vm::onAliasChange,
                label = { Text("Alias / Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = ui.email,
                onValueChange = vm::onEmailChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = ui.password,
                onValueChange = vm::onPasswordChange,
                label = { Text("Contraseña (mín. 6)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            ui.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            val canSubmit = ui.alias.isNotBlank() &&
                    ui.email.contains("@") &&
                    ui.password.length >= 6

            Button(
                onClick = { vm.register { onRegisteredOk() } },
                enabled = canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Creando…")
                } else {
                    Text("Crear cuenta")
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onBackToLogin) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = SkyBlue)
            }
        }
    }
}
