package com.perurest.di


import android.content.Context
import androidx.room.Room
import com.perurest.data.local.AppDatabase
import com.perurest.repository.DishRepository


object ServiceLocator {
    @Volatile private var db: AppDatabase? = null


    fun provideRepository(context: Context): DishRepository {
        val database = db ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "perurest.db"
        ).build().also { db = it }
        return DishRepository(database.dishDao())
    }
}