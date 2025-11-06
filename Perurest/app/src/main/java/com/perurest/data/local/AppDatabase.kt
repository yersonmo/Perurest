package com.perurest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.perurest.data.local.dao.UserDao
import com.perurest.data.local.dao.DishDao
import com.perurest.data.local.entities.UserEntity
import com.perurest.data.local.entities.DishEntity

@Database(
    entities = [UserEntity::class, DishEntity::class], // 👈 lista completa de entidades
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun dishDao(): DishDao
}
