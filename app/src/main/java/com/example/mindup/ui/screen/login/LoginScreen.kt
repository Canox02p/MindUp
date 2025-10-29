package com.example.mindup.ui.screen.login

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private val SkyBlue = Color(0xFF87CEEB) // azul cielo

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginOk: () -> Unit,
    onGoRegister: (() -> Unit)? = null,
    onForgotPassword: (() -> Unit)? = null
) {
    val ui by viewModel.ui.collectAsState()
    val focus = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBlue.copy(alpha = 0.15f)) // fondo azul muy suave
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
            Text(
                "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                color = SkyBlue
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onForgotPassword?.invoke() },
                    enabled = onForgotPassword != null
                ) { Text("¿Olvidaste tu contraseña?", color = SkyBlue) }

                TextButton(
                    onClick = { onGoRegister?.invoke() },
                    enabled = onGoRegister != null
                ) { Text("Crear cuenta", color = SkyBlue) }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    focus.clearFocus()
                    viewModel.login(onLoginOk)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !ui.isLoading && ui.email.isNotBlank() && ui.password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Validando…")
                } else {
                    Text("Entrar", color = Color.White)
                }
            }

            ui.error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

