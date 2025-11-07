package com.perurest.repository

import com.perurest.data.local.dao.UserDao
import com.perurest.data.local.entities.UserEntity

class UserRepository(private val dao: UserDao) {

    /**
     * Valida si las credenciales existen en la base de datos.
     * Retorna true si el usuario existe y la contraseña coincide.
     */
    suspend fun validateCredentials(email: String, password: String): Boolean {
        val user = dao.findByEmail(email)
        return user != null && user.password == password
    }

    /**
     * Registra un nuevo usuario si el correo no está en uso.
     * Retorna true si el registro fue exitoso.
     */
    suspend fun register(name: String, email: String, password: String): Boolean {
        val existing = dao.findByEmail(email)
        if (existing != null) return false // ya existe el usuario

        val newUser = UserEntity(
            name = name,
            email = email,
            password = password
        )
        dao.insert(newUser)
        return true
    }
}
