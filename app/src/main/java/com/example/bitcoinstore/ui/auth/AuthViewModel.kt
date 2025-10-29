package com.example.bitcoinstore.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinstore.data.repo.UserRepository
import com.example.bitcoinstore.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isSignUp: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loggedInUser: User? = null
)

class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui

    fun toggleMode() {
        _ui.value = _ui.value.copy(isSignUp = !_ui.value.isSignUp, error = null)
    }

    fun updateName(v: String)     { _ui.value = _ui.value.copy(name = v) }
    fun updateEmail(v: String)    { _ui.value = _ui.value.copy(email = v) }
    fun updatePassword(v: String) { _ui.value = _ui.value.copy(password = v) }
    fun updateConfirm(v: String)  { _ui.value = _ui.value.copy(confirm = v) }

    fun submit() {
        val s = _ui.value
        _ui.value = s.copy(error = null)

        if (!s.email.contains("@")) {
            _ui.value = s.copy(error = "E-mail inválido"); return
        }
        if (s.password.length < 6) {
            _ui.value = s.copy(error = "Senha mínima de 6 caracteres"); return
        }
        if (s.isSignUp && s.password != s.confirm) {
            _ui.value = s.copy(error = "Senhas não conferem"); return
        }

        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true)

            if (s.isSignUp) {
                val result = repo.register(s.name, s.email, s.password)
                _ui.value = _ui.value.copy(loading = false)
                result.onSuccess {
                    _ui.value = _ui.value.copy(
                        isSignUp = false, name = "", password = "", confirm = "",
                        error = "Cadastro realizado! Faça login para continuar."
                    )
                }.onFailure { e ->
                    _ui.value = _ui.value.copy(error = e.message ?: "Falha no cadastro")
                }
            } else {
                val result = repo.login(s.email, s.password)
                _ui.value = _ui.value.copy(loading = false)
                result.onSuccess { u ->
                    _ui.value = _ui.value.copy(loggedInUser = u)
                }.onFailure { e ->
                    _ui.value = _ui.value.copy(error = e.message ?: "Falha no login")
                }
            }
        }
    }

    fun logout() {
        _ui.value = AuthUiState(isSignUp = false)
    }
}
