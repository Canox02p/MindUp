package com.example.mindup.data.model

data class Materia(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    // Si en tu base de datos tienes "imagen", agrégalo aquí. Si no, bórralo.
    val imagen: String? = null
)