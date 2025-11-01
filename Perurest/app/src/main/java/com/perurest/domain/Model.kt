package com.perurest.domain


data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)


data class PedidoForm(
    val nombre: String = "",
    val telefono: String = "",
    val correo: String = "",
    val direccion: String = "",
)