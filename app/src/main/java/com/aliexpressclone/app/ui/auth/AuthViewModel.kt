package com.aliexpressclone.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Role
import com.aliexpressclone.app.data.local.entity.User
import com.aliexpressclone.app.data.repository.UserRepository
import com.aliexpressclone.app.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(errorMessage = "Ingresa tu correo y contraseña.")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val user: User? = userRepository.login(email, password)
            if (user == null) {
                _uiState.value = AuthUiState(errorMessage = "Correo o contraseña incorrectos.")
            } else {
                sessionManager.login(user.id)
                _uiState.value = AuthUiState()
                onSuccess()
            }
        }
    }

    fun register(name: String, email: String, password: String, role: Role, onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(errorMessage = "Completa todos los campos.")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = userRepository.register(name, email, password, role)
            result.onSuccess { user ->
                sessionManager.login(user.id)
                _uiState.value = AuthUiState()
                onSuccess()
            }.onFailure { error ->
                _uiState.value = AuthUiState(errorMessage = error.message ?: "No se pudo registrar la cuenta.")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
