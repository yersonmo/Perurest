package com.perurest.domain.model

import com.perurest.domain.model.Dish


data class Dish(
    val id: Int,
    val name: String,
    val description: String? = null,
    val price: Double,
    val imageUrl: String? = null
)
