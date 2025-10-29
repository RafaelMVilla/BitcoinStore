package com.example.bitcoinstore.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcoinstore.data.repo.UserRepository
import com.example.bitcoinstore.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserUiState(
    val email: String = "",
    val name: String = "",
    val loading: Boolean = false,
    val message: String? = null
)

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _ui = MutableStateFlow(UserUiState())
    val ui: StateFlow<UserUiState> = _ui

    fun load(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch {
            _ui.value = _ui.value.copy(loading = true, email = email)
            val user = repo.getUser(email)
            _ui.value = if (user != null) {
                UserUiState(email = user.email, name = user.name, loading = false)
            } else {
                UserUiState(email = email, name = "", loading = false, message = "Usuário não encontrado")
            }
        }
    }

    fun updateName(newName: String) {
        _ui.value = _ui.value.copy(name = newName)
    }

    fun saveName() {
        val email = _ui.value.email
        val name = _ui.value.name
        if (email.isBlank() || name.isBlank()) return
        viewModelScope.launch {
            repo.updateName(email, name)
            _ui.value = _ui.value.copy(message = "Nome atualizado!")
        }
    }

    fun changePassword(newPassword: String) {
        val email = _ui.value.email
        if (email.isBlank() || newPassword.length < 6) {
            _ui.value = _ui.value.copy(message = "Senha deve ter pelo menos 6 caracteres.")
            return
        }
        viewModelScope.launch {
            repo.updatePassword(email, newPassword)
            _ui.value = _ui.value.copy(message = "Senha atualizada!")
        }
    }

    fun clearMessage() {
        _ui.value = _ui.value.copy(message = null)
    }
}
