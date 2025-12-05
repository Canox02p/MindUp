package com.example.mindup.data.model

// Esto es el "sobre" con el email y contraseña que envías al servidor
data class LoginRequest(
    val email: String,
    val password: String
)