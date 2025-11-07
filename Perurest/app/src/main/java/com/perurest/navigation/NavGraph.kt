package com.perurest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.perurest.ui.screens.MenuScreen
import com.perurest.ui.screens.PedidoFormScreen
import com.perurest.ui.screens.PlatoDetalleScreen
import com.perurest.ui.screens.PresentationScreen

// Rutas centralizadas
sealed class Route(val route: String) {
    data object Presentacion : Route("presentacion")
    data object Menu : Route("menu")
    data object Plato : Route("plato/{id}") {
        fun create(id: Int) = "plato/$id"
    }
    data object PedidoForm : Route("pedido_form")
}

@Composable
fun PerurestApp() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Route.Presentacion.route
    ) {
        composable(Route.Presentacion.route) {
            // 👇 Usa el nombre real del composable
            PresentationScreen(onStart = { nav.navigate(Route.Menu.route) })
        }

        composable(Route.Menu.route) {
            MenuScreen(
                onDishClick = { id -> nav.navigate(Route.Plato.create(id)) },
                onGoToForm = { nav.navigate(Route.PedidoForm.route) }
            )
        }

        composable(Route.PedidoForm.route) {
            PedidoFormScreen(onBack = { nav.popBackStack() })
        }

        composable(
            route = Route.Plato.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: 0
            PlatoDetalleScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}
