package com.example.mindup.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindup.data.repository.UserRepository
import com.example.mindup.data.prefs.UserPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val repo: UserRepository,
    private val prefs: UserPrefs
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(value: String) {
        _ui.value = _ui.value.copy(email = value, error = null)
    }

    fun onPasswordChange(value: String) {
        _ui.value = _ui.value.copy(password = value, error = null)
    }

    fun login(onSuccess: () -> Unit) {
        val s = _ui.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _ui.value = s.copy(error = "Completa todos los campos")
            return
        }

        viewModelScope.launch {
            _ui.value = s.copy(isLoading = true, error = null)
            val result = repo.login(s.email, s.password)
            result.onSuccess {
                prefs.setLoggedIn(true)
                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            }.onFailure {
                _ui.value = _ui.value.copy(isLoading = false, error = it.message)
            }
        }
    }
}
