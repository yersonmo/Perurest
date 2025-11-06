package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.perurest.domain.Dish


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetailScreen(
    id: Int,
    onBack: () -> Unit,
    onAddClick: (Dish) -> Unit   // ✅ No “onAddToCart”
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Plato") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Atrás") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalle del plato con ID: $id", style = MaterialTheme.typography.titleMedium)
            Text("Aquí puedes mostrar ingredientes, precio, etc.")
        }
    }
}
