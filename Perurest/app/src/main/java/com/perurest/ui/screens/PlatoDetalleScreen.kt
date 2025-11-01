package com.perurest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.perurest.data.SampleSeed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetalleScreen(id: Int, onBack: () -> Unit) {
    val dish = SampleSeed.dishes.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = dish?.name ?: "Plato") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(text = "Atrás") }
                }
            )
        }
    ) { padding ->
        if (dish != null) {
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = dish.imageUrl,
                    contentDescription = dish.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
                Text(text = dish.description, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "$${dish.price}",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = { /* acción de pedido */ }) {
                    Text(text = "Agregar al pedido")
                }
            }
        } else {
            Box(Modifier.padding(padding).padding(16.dp)) {
                Text(text = "Plato no encontrado")
            }
        }
    }
}
