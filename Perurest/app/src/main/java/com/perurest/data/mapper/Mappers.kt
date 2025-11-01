package com.perurest.data.mapper


import com.perurest.data.local.DishEntity
import com.perurest.domain.Dish


fun DishEntity.toDomain() = Dish(id, name, description, price, imageUrl)
fun Dish.toEntity() = DishEntity(id, name, description, price, imageUrl)