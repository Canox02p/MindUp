package com.example.mindup.data.repository

import com.example.mindup.data.prefs.UserPrefs
import kotlinx.coroutines.flow.first

class UserRepository(private val prefs: UserPrefs) {

    suspend fun login(email: String, password: String): Result<Unit> {
        val acc = prefs.account.first()
        return if (acc.email.equals(email.trim(), ignoreCase = true) &&
            acc.password == password
        ) {
            prefs.setLoggedIn(true)
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
        }
    }

    suspend fun register(alias: String, email: String, password: String): Result<Unit> {
        if (alias.isBlank() || email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Datos inválidos"))
        }
        prefs.setAccount(alias.trim(), email.trim(), password)
        prefs.setLoggedIn(true)
        return Result.success(Unit)
    }

    suspend fun recover(email: String, newPassword: String): Result<Unit> {
        val acc = prefs.account.first()
        if (!acc.email.equals(email.trim(), ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("El correo no coincide con la cuenta guardada"))
        }
        if (newPassword.length < 6) {
            return Result.failure(IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres"))
        }
        prefs.setAccount(acc.alias, acc.email, newPassword)
        return Result.success(Unit)
    }
}
