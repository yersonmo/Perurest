package com.perurest.data


import com.perurest.domain.Dish


object SampleSeed {
    val dishes = listOf(
        Dish(1, "Ceviche Limeño", "Pescado fresco con limón y ají limo.", 28.0, "https://picsum.photos/seed/ceviche/200/200"),
        Dish(2, "Lomo Saltado", "Salteado de res con papas y arroz.", 32.0, "https://picsum.photos/seed/lomo/200/200"),
        Dish(3, "Aji de Gallina", "Crema de ají amarillo con pollo.", 24.0, "https://picsum.photos/seed/aji/200/200"),
        Dish(4, "Anticuchos", "Brochetas de corazón a la parrilla.", 22.0, "https://picsum.photos/seed/anticucho/200/200")
    )
}