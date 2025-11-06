package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perurest.domain.model.Dish
import com.perurest.ui.viewmodel.CartUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartUiState,
    onRemove: (Dish) -> Unit,     // <<— DishModel
    onClear: () -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de compras") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } },
                actions = {
                    if (state.items.isNotEmpty()) {
                        TextButton(onClick = onClear) { Text("Vaciar") }
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.items.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                state.items.forEach { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(item.dish.name)
                                val price: Double = item.dish.price   // <-- sin Elvis
                                Text("x${item.qty} • S/ %.2f c/u".format(price))
                            }
                            Text("S/ %.2f".format(item.subtotal))
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = { onRemove(item.dish) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }

                // Divider de Material3 actualizado
                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total")
                    Text("S/ %.2f".format(state.total))
                }

                Button(
                    onClick = onCheckout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Proceder al pago")
                }
            }
        }
    }
}
