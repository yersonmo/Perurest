package com.perurest.ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.perurest.ui.viewmodel.MenuViewModel
import com.perurest.ui.components.DishCard
import com.perurest.ui.components.SearchField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onDishClick: (Int) -> Unit,
    onGoToForm: () -> Unit,
    vm: MenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val filtered = state.items.filter { it.name.contains(state.query, ignoreCase = true) }


    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("A Big Title") }) },
        floatingActionButton = { ExtendedFloatingActionButton(onClick = onGoToForm) { Text("Hacer pedido") } }
    ) { padding ->
        Column(Modifier.padding(padding)) {
// Hero (placeholder)
            Surface(Modifier.fillMaxWidth().height(140.dp)) {}


            SearchField(value = state.query, onValueChange = vm::onQueryChange)


            AnimatedVisibility(visible = state.loading) {
                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                    CircularProgressIndicator()
                }
            }


            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filtered) { dish ->
                    DishCard(
                        title = dish.name,
                        description = dish.description,
                        price = dish.price,
                        imageUrl = dish.imageUrl,
                        onClick = { onDishClick(dish.id) }
                    )
                }
            }
        }
    }
}