package com.example.mindup.data.model

import com.google.gson.annotations.SerializedName

// Esto es lo que el servidor te devuelve (el token)
data class LoginResponse(
    @SerializedName("access_token") val token: String,
    val message: String
)