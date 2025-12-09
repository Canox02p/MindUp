package com.example.mindup.data.repository

import com.example.mindup.data.model.RegisterRequest
import com.example.mindup.data.network.RetrofitInstance
import com.example.mindup.data.prefs.UserPrefs
import kotlinx.coroutines.flow.first

class UserRepository(private val prefs: UserPrefs) {

    // 1. Agregamos la conexión a internet (Lo nuevo)
    private val api = RetrofitInstance.api

    // --- LOGIN (Lógica mixta) ---
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            // A. Primero intentamos loguear en el SERVIDOR (Laravel)
            // (Aquí deberías crear un LoginRequest, similar al de registro)
            // val response = api.login(LoginRequest(email, password))

            // POR AHORA: Usaré tu lógica original local mientras terminas la API de login
            // ESTO ES TU CÓDIGO ORIGINAL:
            val acc = prefs.account.first()
            if (acc.email.equals(email.trim(), ignoreCase = true) && acc.password == password) {
                prefs.setLoggedIn(true)
                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- REGISTRO (Aquí fusionamos lo nuevo con lo tuyo) ---
    suspend fun register(
        name: String,
        email: String,
        password: String,
        telefono: String,     // Nuevo campo
        descripcion: String   // Nuevo campo
    ): Result<Unit> {

        // 1. Validaciones básicas (TU CÓDIGO ORIGINAL)
        if (name.isBlank() || email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Datos inválidos"))
        }

        return try {
            // 2. Enviamos a Laravel (LO NUEVO)
            val request = RegisterRequest(
                name = name,
                email = email,
                password = password,
                telefono = telefono,
                descripcion = descripcion
            )
            val response = api.registrarUsuario(request)

            // 3. Si Laravel dice que sí... guardamos en el celular (TU CÓDIGO ORIGINAL)
            if (response.isSuccessful) {
                // ¡Aquí recuperamos tu lógica!
                // Guardamos en local para que la app sepa que ya está registrado y logueado
                prefs.setAccount(name.trim(), email.trim(), password)
                prefs.setLoggedIn(true)

                Result.success(Unit)
            } else {
                // Si Laravel falla (ej. email repetido)
                val errorMsg = response.errorBody()?.string() ?: "Error en el servidor"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- RESTO DE TUS FUNCIONES (Las dejamos igual por ahora) ---
    suspend fun recover(email: String, newPassword: String): Result<Unit> {
        val acc = prefs.account.first()
        if (!acc.email.equals(email.trim(), ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("El correo no coincide"))
        }
        if (newPassword.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña es muy corta"))
        }
        prefs.setAccount(acc.alias, acc.email, newPassword)
        return Result.success(Unit)
    }

    suspend fun getAccountOnce() = prefs.account.first()

    suspend fun updateAccount(alias: String, email: String): Result<Unit> {
        val acc = prefs.account.first()
        if (alias.isBlank() || email.isBlank()) {
            return Result.failure(IllegalArgumentException("Campos vacíos"))
        }
        prefs.setAccount(alias.trim(), email.trim(), acc.password)
        return Result.success(Unit)
    }
}

