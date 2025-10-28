package com.example.mindup.data.repository

import kotlinx.coroutines.delay

class UserRepository {
    // Simula login. Cambia esto por tu llamada a API cuando la tengas.
    suspend fun login(email: String, password: String): Result<String> {
        delay(1000)
        return if (email.endsWith("@example.com") && password.length >= 6) {
            Result.success("fake_token_123")
        } else {
            Result.failure(Exception("Usuario o contraseña incorrectos"))
        }
    }
}
