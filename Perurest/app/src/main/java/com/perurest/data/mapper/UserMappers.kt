package com.perurest.data.mapper

import com.perurest.data.local.UserEntity
import com.perurest.domain.User

fun UserEntity.toDomain() = User(id, name, email, passwordHash)
fun User.toEntity() = UserEntity(id, name, email, passwordHash)