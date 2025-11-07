package com.perurest.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.perurest.ui.viewmodel.state.CartItem
import com.perurest.ui.viewmodel.state.CartState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// Modelo de UI
import com.perurest.domain.model.Dish as DishModel

class CartViewModel : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    fun add(dish: DishModel) {
        _state.update { old ->
            val idx = old.items.indexOfFirst { it.dish.id == dish.id }
            val newItems =
                if (idx >= 0) {
                    old.items.toMutableList().also { list ->
                        val it = list[idx]
                        list[idx] = it.copy(qty = it.qty + 1)
                    }
                } else {
                    old.items + CartItem(dish = dish, qty = 1)
                }
            old.copy(items = newItems)
        }
    }

    fun remove(dish: DishModel) {
        _state.update { old ->
            val idx = old.items.indexOfFirst { it.dish.id == dish.id }
            if (idx < 0) return@update old
            val list = old.items.toMutableList()
            val it = list[idx]
            val new =
                if (it.qty > 1) {
                    list[idx] = it.copy(qty = it.qty - 1)
                    list.toList()
                } else {
                    list.removeAt(idx)
                    list.toList()
                }
            old.copy(items = new)
        }
    }

    fun clear() {
        _state.update { it.copy(items = emptyList()) }
    }
}
