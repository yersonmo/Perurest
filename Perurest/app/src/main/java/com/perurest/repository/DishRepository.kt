package com.perurest.repository

import com.perurest.data.local.dao.DishDao
import com.perurest.data.local.entities.DishEntity
import com.perurest.domain.model.Dish
import com.perurest.data.SampleSeed // si lo tienes en otro paquete, ajusta el import
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DishRepository(private val dao: DishDao) {

    /**
     * Devuelve el menú como Flow de dominio (List<Dish>), mapeando desde las entidades Room.
     */
    fun getMenu(): Flow<List<Dish>> =
        dao.getAllAsFlow().map { list -> list.map { it.toDomain() } }

    /**
     * Si necesitas precargar datos de ejemplo.
     * - Si tienes dao.count(): siembra solo cuando está vacío.
     * - Si NO tienes count(), simplemente quita el if y siempre upsertea.
     */
    suspend fun seedIfEmpty() {
        // Quita este bloque 'if' si no tienes count()
        val isEmpty = try {
            dao.count() == 0
        } catch (_: Throwable) {
            // Si tu DAO no implementa count(), comenta lo de arriba y deja el upsert siempre
            true
        }

        if (isEmpty) {
            dao.upsertAll(SampleSeed.dishes.map { it.toEntity() })
        }
    }
}

/* ===================== */
/* ====  MAPEADORES  ==== */
/* ===================== */

// Ajusta estos mapeos al shape real de tus clases.
private fun DishEntity.toDomain(): Dish = Dish(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl
)

private fun Dish.toEntity(): DishEntity = DishEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    imageUrl = imageUrl
)
