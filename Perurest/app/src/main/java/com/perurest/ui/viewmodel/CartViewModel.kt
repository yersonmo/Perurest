package com.perurest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perurest.domain.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CartItem(
    val dish: Dish,
    val qty: Int
) {
    val subtotal: Double get() = dish.price * qty
}


data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val count: Int = 0
)

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())

    /** Cantidad total de unidades en el carrito */
    val count: StateFlow<Int> = _items
        .map { list -> list.sumOf { it.qty } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    /** Estado de UI derivado de _items (items, total y count) */
    val state: StateFlow<CartUiState> = _items
        .map { list ->
            CartUiState(
                items = list,
                total = list.sumOf { it.subtotal },
                count = list.sumOf { it.qty }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CartUiState()
        )

    /* ----------------- Acciones públicas ----------------- */

    fun add(dish: com.perurest.domain.Dish) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.dish.id == dish.id }
        if (idx >= 0) {
            current[idx] = current[idx].copy(qty = current[idx].qty + 1)
        } else {
            current += CartItem(dish, 1)
        }
        _items.value = current
    }

    fun remove(dish: Dish) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.dish.id == dish.id }
        if (idx >= 0) {
            current.removeAt(idx)
            _items.value = current
        }
    }

    fun removeById(dishId: Int) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.dish.id == dishId }
        if (idx >= 0) {
            current.removeAt(idx)
            _items.value = current
        }
    }

    fun inc(dishId: Int) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.dish.id == dishId }
        if (idx >= 0) {
            current[idx] = current[idx].copy(qty = current[idx].qty + 1)
            _items.value = current
        }
    }

    fun dec(dishId: Int) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.dish.id == dishId }
        if (idx >= 0) {
            val newQty = current[idx].qty - 1
            if (newQty <= 0) current.removeAt(idx)
            else current[idx] = current[idx].copy(qty = newQty)
            _items.value = current
        }
    }

    fun clear() {
        _items.value = emptyList()
    }
}
