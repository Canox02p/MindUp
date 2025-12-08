package com.example.mindup.data.model

data class Materia(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val imagen: String? = null,
    val iconoLocal: Int = 0
)