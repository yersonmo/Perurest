package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.perurest.domain.model.Dish
import com.perurest.ui.viewmodel.MenuUiState
import java.util.Locale
import com.perurest.ui.components.DishCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    state: MenuUiState,
    cartCount: Int,
    onQueryChange: (String) -> Unit,
    onPlatoClick: (Int) -> Unit,
    onAddToCart: (Dish) -> Unit,
    onOpenCart: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Menú") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onOpenCart) {
                Text("Carrito ($cartCount)")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                label = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            when {
                state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.error != null -> Text(state.error, color = MaterialTheme.colorScheme.error)
                state.items.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay platos disponibles.")
                }
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(state.items, key = { it.id }) { dish ->
                        DishCard(
                            title = dish.name,
                            description = dish.description ?: "",
                            imageUrl = dish.imageUrl ?: "",
                            // si price es Double? usa el Elvis; si es Double no nulo, usa solo dish.price
                            price = dish.price,
                            onClick = { onPlatoClick(dish.id) },
                            onAddClick = { onAddToCart(dish) }   // 👈 ahora sí usas onAddToCart
                        )
                    }

                }
            }
        }
    }
}
