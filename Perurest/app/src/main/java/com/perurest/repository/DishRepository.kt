package com.perurest.repository


import com.perurest.data.local.DishDao
import com.perurest.data.mapper.toDomain
import com.perurest.data.mapper.toEntity
import com.perurest.data.SampleSeed
import com.perurest.domain.Dish
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DishRepository(private val dao: DishDao) {
    fun getMenu(): Flow<List<Dish>> = dao.getAll().map { list -> list.map { it.toDomain() } }


    suspend fun seedIfEmpty() {
// naive seeding: always upsert sample items
        dao.upsertAll(SampleSeed.dishes.map { it.toEntity() })
    }
}