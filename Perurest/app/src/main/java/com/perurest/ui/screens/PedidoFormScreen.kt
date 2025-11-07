package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoFormScreen(
    onPedidoScreenCallback: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Formulario de Pedido") })
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Aquí irá el formulario de pedido", style = MaterialTheme.typography.titleMedium)

            Button(onClick = onPedidoScreenCallback, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Menú")
            }
        }
    }
}
