package com.perurest.ui.viewmodel.state

// Modelo de UI
import com.perurest.domain.model.Dish as DishModel

data class CartItem(
    val dish: DishModel,
    val qty: Int
)

data class CartState(
    val items: List<CartItem> = emptyList()
) {
    val total: Double get() = items.sumOf { it.dish.price * it.qty }
    val count: Int get() = items.sumOf { it.qty }
}
