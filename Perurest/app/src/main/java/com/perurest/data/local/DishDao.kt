package com.perurest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perurest.data.local.entities.DishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Query("SELECT * FROM dishes ORDER BY name")
    fun getAllAsFlow(): Flow<List<DishEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<DishEntity>)

    @Query("SELECT COUNT(*) FROM dishes")
    suspend fun count(): Int
}
