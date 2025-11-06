package com.perurest.data

import com.perurest.domain.model.Dish

object SampleSeed {
    val dishes = listOf(
        Dish(
            id = 1,
            name = "Ceviche",
            description = "Clásico peruano de pescado",
            price = 28.0,
            imageUrl = null
        ),
        Dish(
            id = 2,
            name = "Lomo Saltado",
            description = "Salteado de lomo con papas",
            price = 32.0,
            imageUrl = null
        ),
        Dish(
            id = 3,
            name = "Ají de Gallina",
            description = "Crema amarilla tradicional",
            price = 24.0,
            imageUrl = null
        )
    )
}
