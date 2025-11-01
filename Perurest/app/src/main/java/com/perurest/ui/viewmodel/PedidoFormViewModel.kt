package com.perurest.ui.viewmodel


import androidx.lifecycle.ViewModel
import com.perurest.domain.PedidoForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class FormErrors(
    val nombre: String? = null,
    val telefono: String? = null,
    val correo: String? = null,
    val direccion: String? = null
)


data class PedidoFormUiState(
    val data: PedidoForm = PedidoForm(),
    val errors: FormErrors = FormErrors(),
    val submitting: Boolean = false,
    val submittedOk: Boolean = false
)


class PedidoFormViewModel : ViewModel() {
    private val _state = MutableStateFlow(PedidoFormUiState())
    val state: StateFlow<PedidoFormUiState> = _state.asStateFlow()


    fun updateNombre(v: String) = _state.update { it.copy(data = it.data.copy(nombre = v)) }
    fun updateTelefono(v: String) = _state.update { it.copy(data = it.data.copy(telefono = v)) }
    fun updateCorreo(v: String) = _state.update { it.copy(data = it.data.copy(correo = v)) }
    fun updateDireccion(v: String) = _state.update { it.copy(data = it.data.copy(direccion = v)) }


    fun submit() {
        val s = _state.value
        val e = FormErrors(
            nombre = s.data.nombre.takeIf { it.isBlank() }?.let { "Requerido" },
            telefono = s.data.telefono.takeIf { it.length < 8 }?.let { "Mínimo 8 dígitos" },
            correo = s.data.correo.takeIf { !it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) }?.let { "Correo inválido" },
            direccion = s.data.direccion.takeIf { it.isBlank() }?.let { "Requerido" }
        )
        val hasErrors = listOf(e.nombre, e.telefono, e.correo, e.direccion).any { it != null }
        _state.update { it.copy(errors = e, submitting = !hasErrors, submittedOk = !hasErrors) }
    }
}