package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perurest.domain.model.Dish as DishModel


@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetailScreen(
    id: Int,
    onBack: () -> Unit,
    onAddClick: (DishModel) -> Unit
) {
    // Mock simple para compilar y usar el callback
    val dish = remember(id) {
        DishModel(
            id = id,
            name = "Detalle del Plato #$id",
            description = "Descripción breve del plato seleccionado.",
            price = 9.90,
            imageUrl = ""
        )
    }

    // Usamos CenterAlignedTopAppBar (estable) para evitar @OptIn
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Plato") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = dish.name, style = MaterialTheme.typography.titleLarge)
            Text("Precio: $${"%.2f".format(dish.price)}")
            Text(text = "Precio: $${"%.2f".format(dish.price)}")

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onAddClick(dish) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}
