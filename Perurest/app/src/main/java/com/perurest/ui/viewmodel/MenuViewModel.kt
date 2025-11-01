package com.perurest.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.perurest.di.ServiceLocator
import com.perurest.domain.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MenuUiState(
    val loading: Boolean = true,
    val query: String = "",
    val items: List<Dish> = emptyList()
)


class MenuViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ServiceLocator.provideRepository(app)


    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()


    init {
        viewModelScope.launch {
            repo.seedIfEmpty()
            repo.getMenu().collect { dishes ->
                _state.update { it.copy(loading = false, items = dishes) }
            }
        }
    }


    fun onQueryChange(text: String) { _state.update { it.copy(query = text) } }
}