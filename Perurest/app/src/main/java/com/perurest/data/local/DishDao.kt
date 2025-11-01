package com.perurest.data.local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface DishDao {
    @Query("SELECT * FROM dishes ORDER BY id")
    fun getAll(): Flow<List<DishEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<DishEntity>)
}