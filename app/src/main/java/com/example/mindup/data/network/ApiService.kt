package com.example.mindup.data.network

import com.example.mindup.data.model.LoginRequest
import com.example.mindup.data.model.LoginResponse
import com.example.mindup.data.model.Materia // 👈 Importamos el molde nuevo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Login (Ya lo tenías)
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // 👇 NUEVA FUNCIÓN: Pedir materias
    // Fíjate que pedimos el "token" como parámetro para enviarlo en la cabecera
    @GET("mis-materias")
    fun getMisMaterias(@Header("Authorization") token: String): Call<List<Materia>>
}