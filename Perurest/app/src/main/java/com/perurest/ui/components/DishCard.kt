package com.perurest.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun DishCard(
    title: String,
    description: String,
    imageUrl: String,
    price: Double,
    onClick: () -> Unit,
    onAddClick: () -> Unit,   // <- nombre definitivo para evitar el error
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .size(72.dp)
                    .padding(end = 12.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text("S/ " + String.format(Locale.US, "%.2f", price))
            }

            Button(onClick = onAddClick) { Text("Añadir") }
        }
    }
}
