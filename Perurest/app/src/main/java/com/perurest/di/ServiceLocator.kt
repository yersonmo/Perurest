package com.perurest.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.perurest.data.local.AppDatabase
import com.perurest.repository.UserRepository
import com.perurest.session.SessionStore
import com.perurest.ui.viewmodel.AuthViewModel

// 👇 imports NUEVOS (ajusta los paquetes si en tu proyecto son distintos)
import com.perurest.repository.DishRepository
import com.perurest.data.local.dao.DishDao

object ServiceLocator {

    lateinit var appContext: Context
        private set

    fun init(ctx: Context) {
        appContext = ctx.applicationContext
    }

    // --- Base de datos / DAO ---
    private val db: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "perurest_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    // --- Auth ---
    private val userRepo: UserRepository by lazy { UserRepository(dao = db.userDao()) }
    private val session: SessionStore by lazy { SessionStore(appContext) }

    fun authVmFactory(): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(userRepo, session) as T
            }
        }

    // --- Repo de Platos (Menu) ---
    // ⚠️ Requiere que AppDatabase tenga fun dishDao(): DishDao (ver paso 2 abajo)
    private val dishRepo: DishRepository by lazy { DishRepository(db.dishDao()) }

    // Forma de acceso (elige una y usa la misma en tu ViewModel)
    fun getDishRepository(): DishRepository = dishRepo
    // o: val dishRepository: DishRepository get() = dishRepo
}
