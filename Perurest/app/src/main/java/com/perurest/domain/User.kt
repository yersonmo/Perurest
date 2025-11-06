package com.perurest.domain

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String // guarda hash, no la contraseña
)