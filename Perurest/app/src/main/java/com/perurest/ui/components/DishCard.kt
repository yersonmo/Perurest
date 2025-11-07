package com.perurest.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun DishCard(
    title: String,
    description: String,
    price: Double,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(model = imageUrl, contentDescription = title, modifier = Modifier.size(72.dp))
            Column(Modifier.weight(1f)) {
                Text(title)
                Text(description, maxLines = 2)
                Text((price * 1000).toChileanPeso())
            }
        }
    }
}