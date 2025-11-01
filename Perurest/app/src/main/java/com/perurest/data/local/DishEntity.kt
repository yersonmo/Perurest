package com.perurest.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)