package com.perurest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.perurest.model.AppDatabase
import com.perurest.repository.RepositorioComida
import com.perurest.ui.theme.AppComidaPeruana
import com.perurest.viewmodel.ComidaViewModel
import com.perurest.viewmodel.ComidaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Instancia de Base de Datos y Repositorio
        val database = AppDatabase.getDatabase(this)
        val repositorio = RepositorioComida(database)

        // 2. ViewModel Factory
        val viewModelFactory = ComidaViewModelFactory(repositorio)
        val viewModel = ViewModelProvider(this, viewModelFactory)[ComidaViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppComidaPeruana(
                        viewModel = viewModel,
                        onExitApp = {
                            Toast.makeText(this, "Gracias por visitarnos", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    )
                }
            }
        }
    }
}