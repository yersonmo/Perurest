package com.perurest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perurest.di.ServiceLocator
import com.perurest.domain.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class MenuUiState(
    val query: String = "",
    val loading: Boolean = true,
    val items: List<Dish> = emptyList(),
    val error: String? = null
)

class MenuViewModel : ViewModel() {

    private val repo = ServiceLocator.getDishRepository()

    private val _query = MutableStateFlow("")
    private val _items = MutableStateFlow<List<Dish>>(emptyList())
    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        // 1) Cargar y observar el menú
        viewModelScope.launch {
            try {
                repo.seedIfEmpty() // opcional
                repo.getMenu().collectLatest { dishes ->
                    _items.value = dishes
                    _loading.value = false
                    _error.value = null
                }
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Error cargando el menú"
            }
        }

        // 2) Construir el estado combinando query + items + loading + error
        viewModelScope.launch {
            combine(_query, _items, _loading, _error) { q, items, loading, error ->
                val filtered = if (q.isBlank()) items
                else items.filter {
                    it.name.contains(q, ignoreCase = true) ||
                            (it.description ?: "").contains(q, ignoreCase = true)
                }
                MenuUiState(
                    query = q,
                    loading = loading,
                    items = filtered,
                    error = error
                )
            }.collectLatest { newState -> _state.value = newState }
        }
    }

    fun onQueryChange(text: String) {
        _query.value = text
    }
}
