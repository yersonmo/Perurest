package com.perurest.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.perurest.ui.viewmodel.PedidoFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoFormScreen(
    onBack: () -> Unit,
    vm: PedidoFormViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de entrega") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.data.nombre,
                onValueChange = vm::updateNombre,
                label = { Text("Nombre") },
                isError = state.errors.nombre != null,
                supportingText = { state.errors.nombre?.let { Text(it) } },
                trailingIcon = {
                    if (state.errors.nombre != null)
                        Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.data.telefono,
                onValueChange = vm::updateTelefono,
                label = { Text("Teléfono") },
                isError = state.errors.telefono != null,
                supportingText = { state.errors.telefono?.let { Text(it) } },
                trailingIcon = {
                    if (state.errors.telefono != null)
                        Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.data.correo,
                onValueChange = vm::updateCorreo,
                label = { Text("Correo") },
                isError = state.errors.correo != null,
                supportingText = { state.errors.correo?.let { Text(it) } },
                trailingIcon = {
                    if (state.errors.correo != null)
                        Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.data.direccion,
                onValueChange = vm::updateDireccion,
                label = { Text("Dirección") },
                isError = state.errors.direccion != null,
                supportingText = { state.errors.direccion?.let { Text(it) } },
                trailingIcon = {
                    if (state.errors.direccion != null)
                        Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = vm::submit, enabled = !state.submitting) {
                Text("Confirmar pedido")
            }

            AnimatedVisibility(visible = state.submittedOk) {
                Text("¡Datos válidos! Se procederá con el pedido.")
            }
        }
    }
}
