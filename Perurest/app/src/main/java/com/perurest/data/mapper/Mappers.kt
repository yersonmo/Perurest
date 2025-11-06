package com.perurest.data.mapper

import com.perurest.domain.Dish as DishDomain
import com.perurest.domain.model.Dish as DishModel


fun DishDomain.toModel(): DishModel = DishModel(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    price = price
)
