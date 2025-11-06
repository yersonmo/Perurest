package com.perurest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perurest.repository.UserRepository
import com.perurest.session.SessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepo: UserRepository,
    private val session: SessionStore
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    // --- Intents ---
    fun onName(v: String)      { _state.value = _state.value.copy(name = v, errorName = null) }
    fun onEmail(v: String)     { _state.value = _state.value.copy(email = v, errorEmail = null) }
    fun onPassword(v: String)  { _state.value = _state.value.copy(password = v, errorPassword = null) }
    fun onConfirmPassword(v: String) { _state.value = _state.value.copy(confirmPassword = v, errorConfirmPassword = null) }

    // --- Login ---
    fun doLogin() {
        val st = _state.value
        var errEmail: String? = null
        var errPass: String? = null

        if (!st.email.contains("@")) errEmail = "Correo inválido"
        if (st.password.length < 4) errPass = "Mínimo 4 caracteres"

        if (errEmail != null || errPass != null) {
            _state.value = st.copy(errorEmail = errEmail, errorPassword = errPass)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val ok = userRepo.validateCredentials(st.email, st.password)
            if (ok) {
                session.saveLoggedUser(st.email)
                _state.value = _state.value.copy(loading = false, isLogged = true)
            } else {
                _state.value = _state.value.copy(loading = false, error = "Credenciales inválidas")
            }
        }
    }

    // --- Register ---
    fun doRegister() {
        val st = _state.value
        var errName: String? = null
        var errEmail: String? = null
        var errPass: String? = null
        var errPass2: String? = null

        if (st.name.isBlank()) errName = "Requerido"
        if (!st.email.contains("@")) errEmail = "Correo inválido"
        if (st.password.length < 4) errPass = "Mínimo 4 caracteres"
        if (st.confirmPassword != st.password) errPass2 = "No coincide"

        if (listOf(errName, errEmail, errPass, errPass2).any { it != null }) {
            _state.value = st.copy(
                errorName = errName, errorEmail = errEmail,
                errorPassword = errPass, errorConfirmPassword = errPass2
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val ok = userRepo.register(st.name, st.email, st.password)
            if (ok) {
                session.saveLoggedUser(st.email)
                _state.value = _state.value.copy(loading = false, isLogged = true)
            } else {
                _state.value = _state.value.copy(loading = false, error = "No se pudo registrar")
            }
        }
    }
}
