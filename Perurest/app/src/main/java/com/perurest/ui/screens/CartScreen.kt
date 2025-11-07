package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import com.perurest.ui.viewmodel.state.CartState
import com.perurest.domain.model.Dish as DishModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartState,
    onRemove: (DishModel) -> Unit,
    onClear: () -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    if (state.items.isNotEmpty()) {
                        TextButton(onClick = onClear) { Text("Vaciar") }
                    }
                }
            )
        }
    ) { inner ->
        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        items = state.items,
                        key = { it.dish.id }
                    ) { cartItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(cartItem.dish.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "x${cartItem.qty}  •  $${cartItem.dish.price}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            TextButton(onClick = { onRemove(cartItem.dish) }) {
                                Text("Quitar")
                            }
                        }
                        Divider()
                    }
                }

                // Footer con total + botón
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Total: $${"%.2f".format(state.total)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ir al pago (${state.count} ítems)")
                    }
                }
            }
        }
    }
}
