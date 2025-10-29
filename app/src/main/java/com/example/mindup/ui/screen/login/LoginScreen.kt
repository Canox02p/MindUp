package com.example.mindup.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindup.R

private val SkyBlue = Color(0xFF87CEEB)
private val CardBg = Color(0xFFFFFFFF)
private val PageBg = Color(0xFFEFF7FD)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginOk: () -> Unit,
    onGoRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onClickFacebook: (() -> Unit)? = null,
    onClickGoogle:  (() -> Unit)? = null,
    onClickApple:   (() -> Unit)? = null,
) {
    val ui by viewModel.ui.collectAsState()
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(CardBg)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            /*header mini
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_mindup),
                    contentDescription = "MindUp",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 20.dp, y = (-10).dp)
                )
            }
             */

            Spacer(Modifier.height(12.dp))

            // título + logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp), // área que ocupa el logo
                contentAlignment = Alignment.Center
            ) {
                // --- Logo en grande ---
                Image(
                    painter = painterResource(R.drawable.logo_mindup),
                    contentDescription = "Logo MindUp",
                    modifier = Modifier
                        .size(800.dp)
                        .offset(y = -20.dp)
                )


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(y = 80.dp)
                ) {
                    Text(
                        "Bienvenido de regreso",
                        color = Color(0xFF2B9FD6),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Inicia sesión en una cuenta MindUp existente",
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp
                    )
                }
            }


            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6B7280)) },
                label = { Text("USUARIO") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))


            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6B7280)) },
                label = { Text("contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        if (showPassword) "Ocultar" else "Mostrar",
                        color = SkyBlue,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { showPassword = !showPassword }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    "Recuperar Contraseña?",
                    color = SkyBlue,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onForgotPassword() }
                )
            }

            Spacer(Modifier.height(16.dp))

            // botón principal
            Button(
                onClick = { viewModel.login(onLoginOk) },
                enabled = !ui.isLoading && ui.email.isNotBlank() && ui.password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B9FD6)),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                if (ui.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Entrando…", color = Color.White)
                } else {
                    Text("LOG IN", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            ui.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("O regístrate usando", color = Color(0xFF6B7280), fontSize = 12.sp)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIcon(R.drawable.ic_facebook) { onClickFacebook?.invoke() }
                SocialIcon(R.drawable.ic_google)   { onClickGoogle?.invoke() }
                SocialIcon(R.drawable.ic_apple)    { onClickApple?.invoke() }
            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                val text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFF6B7280))) { append("¿No tienes una cuenta? ") }
                    withStyle(SpanStyle(color = SkyBlue, fontWeight = FontWeight.SemiBold)) { append("Regístrate") }
                }
                Text(text = text, modifier = Modifier.clickable { onGoRegister() })
            }
        }
    }
}

@Composable
private fun SocialIcon(drawableId: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF3F4F6))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(drawableId),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
    }
}
