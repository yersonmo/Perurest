package com.perurest.data


import com.perurest.domain.Dish


object SampleSeed {
    val dishes = listOf(
        Dish(1, "Ceviche Limeño", "Pescado fresco con limón y ají limo.", 11.990, "android.resource://com.perurest/drawable/ceviche_limenio"),
        Dish(2, "Lomo Saltado", "Salteado de res con papas y arroz.", 10.990, "android.resource://com.perurest/drawable/lomo_saltado"),
        Dish(3, "Aji de Gallina", "Crema de ají amarillo con pollo.", 9.990, "android.resource://com.perurest/drawable/aji_de_gallina"),
        Dish(4, "Anticuchos", "Brochetas de carne a la parrilla.", 8.990, "android.resource://com.perurest/drawable/anticucho")
    )
}