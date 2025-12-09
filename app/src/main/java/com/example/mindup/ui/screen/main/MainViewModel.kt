package com.example.mindup.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mindup.data.model.Materia
import com.example.mindup.data.prefs.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.mindup.data.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// El estado de la pantalla: ¿Cargando? ¿Hay materias? ¿Hubo error?
data class MainUiState(
    val isLoading: Boolean = false,
    val materias: List<Materia> = emptyList(),
    val error: String? = null
)

class MainViewModel(private val prefs: UserPrefs) : ViewModel() {

    private val _ui = MutableStateFlow(MainUiState())
    val ui: StateFlow<MainUiState> = _ui

    // Apenas nace el ViewModel, intentamos cargar las materias
    init {
        loadMaterias()
    }

    private fun loadMaterias() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true) }

            // 1. Sacamos el token de la caja fuerte
            val token = prefs.authToken.first()

            if (token.isNullOrEmpty()) {
                _ui.update { it.copy(isLoading = false, error = "No hay sesión activa") }
                return@launch
            }

            // 2. Llamamos a la API enviando "Bearer <token>"
            // OJO: Es muy importante poner la palabra "Bearer " antes del token
            RetrofitInstance.api.getMisMaterias("Bearer $token").enqueue(object : Callback<List<Materia>> {

                override fun onResponse(call: Call<List<Materia>>, response: Response<List<Materia>>) {
                    _ui.update { it.copy(isLoading = false) }

                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        Log.d("API_MINDUP", "Materias cargadas: ${lista.size}")
                        _ui.update { it.copy(materias = lista) }
                    } else {
                        Log.e("API_MINDUP", "Error al cargar materias: ${response.code()}")
                        _ui.update { it.copy(error = "Error del servidor: ${response.code()}") }
                    }
                }

                override fun onFailure(call: Call<List<Materia>>, t: Throwable) {
                    Log.e("API_MINDUP", "Fallo de conexión: ${t.message}")
                    _ui.update { it.copy(isLoading = false, error = t.message) }
                }
            })
        }
    }

    // Función para refrescar manualmente (opcional)
    fun refresh() {
        loadMaterias()
    }
}