package com.example.mindup.ui.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
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
import androidx.compose.ui.zIndex
import com.example.mindup.R

private val SkyBlue = Color(0xFF87CEEB)
private val CardBg = Color(0xFFFFFFFF)
private val PageBg = Color(0xFFEFF7FD)

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onGoLogin: () -> Unit
) {
    val ui by viewModel.ui.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    val pushDown = 80.dp

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ===== Título flotante "MindUp" =====
                Text(
                    buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF1B1F23),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W600
                            )
                        ) { append("M") }
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF2B9FD6),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W600
                            )
                        ) { append("indUp") }
                    },
                    modifier = Modifier
                        .offset(x = -110.dp, y = 40.dp)  //  x y
                        .zIndex(3f)
                )

                Spacer(Modifier.height(8.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = pushDown)
            ) {
                Spacer(Modifier.height(12.dp))

                Text(
                    "Listo para poder registrarse",
                    color = Color(0xFF2B9FD6),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Crea un registro de perfil para MindUp",
                    color = Color(0xFF6B7280),
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = ui.username,
                    onValueChange = viewModel::onUsernameChange,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6B7280)) },
                    label = { Text("User Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = ui.email,
                    onValueChange = viewModel::onEmailChange,
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null, tint = Color(0xFF6B7280)) },
                    label = { Text("Email") },
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
                    label = { Text("Password") },
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
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = ui.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6B7280)) },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    singleLine = true,
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            if (showConfirmPassword) "Ocultar" else "Mostrar",
                            color = SkyBlue,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { showConfirmPassword = !showConfirmPassword }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.register(onRegisterSuccess) },
                    enabled = ui.isValid && !ui.isLoading,
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
                        Text("Creando…", color = Color.White)
                    } else {
                        Text("crear", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                ui.error?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    val text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF6B7280))) { append("¿Ya tienes una cuenta? ") }
                        withStyle(SpanStyle(color = SkyBlue, fontWeight = FontWeight.SemiBold)) { append("Inicia sesión aquí.") }
                    }
                    Text(text = text, modifier = Modifier.clickable { onGoLogin() })
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_logo_mindup),
            contentDescription = "MindUp",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 0.dp)
                .size(150.dp)
                .zIndex(2f)
        )
    }
}
