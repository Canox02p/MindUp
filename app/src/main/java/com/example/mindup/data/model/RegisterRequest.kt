package com.example.mindup.data.model

class RegisterRequest(
    name: String,
    email: String,
    password: String,
    telefono: String,
    descripcion: String
) {
    // RegisterRequest.kt
    data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String,
        val telefono: String,      // Vi este campo en tu Postman
        val descripcion: String    // Vi este campo en tu Postman
    )
}