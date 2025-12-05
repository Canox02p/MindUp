package com.example.mindup.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindup.data.model.LoginRequest
import com.example.mindup.data.model.LoginResponse
import com.example.mindup.data.network.RetrofitInstance
import com.example.mindup.data.prefs.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// 👇 AHORA RECIBE "prefs" EN EL PARÉNTESIS
class LoginViewModel(private val prefs: UserPrefs) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(value: String) {
        _ui.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _ui.update { it.copy(password = value, error = null) }
    }

    fun login(onSuccess: () -> Unit) {
        val currentState = _ui.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _ui.update { it.copy(error = "Completa todos los campos") }
            return
        }

        _ui.update { it.copy(isLoading = true, error = null) }

        val request = LoginRequest(currentState.email, currentState.password)

        RetrofitInstance.api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _ui.update { it.copy(isLoading = false) }

                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    Log.d("API_MINDUP", "Login Exitoso. Guardando Token...")

                    // 👇 ¡AQUÍ GUARDAMOS EL TOKEN EN LA CAJA FUERTE!
                    viewModelScope.launch {
                        prefs.saveAuthToken(token)
                        // Después de guardar, avisamos que ya podemos pasar
                        onSuccess()
                    }

                } else {
                    _ui.update { it.copy(error = "Correo o contraseña incorrectos") }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _ui.update { it.copy(isLoading = false, error = "Error de conexión") }
            }
        })
    }
}