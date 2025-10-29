package com.example.mindup.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindup.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isValid: Boolean = false
)

class RegisterViewModel(
    private val repo: UserRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui: StateFlow<RegisterUiState> = _ui

    private fun validate(
        username: String = _ui.value.username,
        email: String = _ui.value.email,
        password: String = _ui.value.password,
        confirmPassword: String = _ui.value.confirmPassword
    ): Boolean {
        val emailOk = email.contains("@") && email.contains(".")
        val passOk = password.length >= 6
        val match = password == confirmPassword
        val userOk = username.isNotBlank()
        return emailOk && passOk && match && userOk
    }

    fun onUsernameChange(v: String) {
        val s = _ui.value.copy(username = v, error = null)
        _ui.value = s.copy(isValid = validate(username = v))
    }

    fun onEmailChange(v: String) {
        val s = _ui.value.copy(email = v, error = null)
        _ui.value = s.copy(isValid = validate(email = v))
    }

    fun onPasswordChange(v: String) {
        val s = _ui.value.copy(password = v, error = null)
        _ui.value = s.copy(isValid = validate(password = v))
    }

    fun onConfirmPasswordChange(v: String) {
        val s = _ui.value.copy(confirmPassword = v, error = null)
        _ui.value = s.copy(isValid = validate(confirmPassword = v))
    }

    fun register(onSuccess: () -> Unit) {
        val s = _ui.value
        if (!s.isValid) {
            _ui.value = s.copy(error = "Revisa usuario, correo y contraseñas (mín. 6 y coinciden)")
            return
        }

        viewModelScope.launch {
            _ui.value = s.copy(isLoading = true, error = null)
            val res = repo.register(alias = s.username, email = s.email, password = s.password)
            res.onSuccess {
                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            }.onFailure {
                _ui.value = _ui.value.copy(isLoading = false, error = it.message ?: "Error al registrar")
            }
        }
    }
}
