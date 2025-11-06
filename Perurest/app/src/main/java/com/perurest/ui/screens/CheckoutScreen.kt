package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perurest.ui.viewmodel.CartUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    state: CartUiState,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de compra") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total: S/ ${"%.2f".format(state.total)}")
                    Button(onClick = onConfirm, enabled = state.items.isNotEmpty()) {
                        Text("Confirmar compra")
                    }
                }
            }
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.items) { it ->
                ListItem(
                    headlineContent = { Text(it.dish.name) },
                    supportingContent = { Text("x${it.qty}  •  S/ ${"%.2f".format(it.subtotal)}") }
                )
                Divider()
            }
        }
    }
}
