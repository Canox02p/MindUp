package com.example.mindup.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindup.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val alias: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel(private val repo: UserRepository) : ViewModel() {

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui: StateFlow<RegisterUiState> = _ui

    fun onAliasChange(v: String) { _ui.value = _ui.value.copy(alias = v, error = null) }
    fun onEmailChange(v: String) { _ui.value = _ui.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String) { _ui.value = _ui.value.copy(password = v, error = null) }

    fun register(onSuccess: () -> Unit) {
        val s = _ui.value
        val valid = s.alias.isNotBlank() &&
                s.email.contains("@") &&
                s.password.length >= 6
        if (!valid) {
            _ui.value = s.copy(error = "Revisa alias, correo y contraseña (mín. 6)")
            return
        }

        viewModelScope.launch {
            _ui.value = s.copy(isLoading = true, error = null)
            val r = repo.register(s.alias, s.email, s.password)
            r.onSuccess {
                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            }.onFailure {
                _ui.value = _ui.value.copy(isLoading = false, error = it.message)
            }
        }
    }
}
