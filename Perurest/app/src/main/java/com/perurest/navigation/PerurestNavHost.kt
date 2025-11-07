package com.perurest.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.perurest.ui.screens.LoginScreen
import com.perurest.ui.screens.RegisterScreen
import com.perurest.ui.screens.MenuScreen
import com.perurest.ui.screens.PlatoDetailScreen
import com.perurest.ui.screens.CartScreen
import com.perurest.ui.screens.CheckoutScreen

import com.perurest.ui.viewmodel.AuthViewModel
import com.perurest.ui.viewmodel.MenuViewModel
import com.perurest.ui.viewmodel.CartViewModel

import com.perurest.di.ServiceLocator

// Usamos SIEMPRE el modelo de UI
import com.perurest.domain.model.Dish as DishModel

private object Routes {
    const val Login = "login"
    const val Register = "register"
    const val Menu = "menu"
    const val Plate = "plate"
    const val Cart = "cart"
    const val Checkout = "checkout"
}

@Composable
fun PerurestNavHost() {
    val nav = rememberNavController()

    val authVm: AuthViewModel = viewModel(factory = ServiceLocator.authVmFactory())
    val menuVm: MenuViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    NavHost(navController = nav, startDestination = Routes.Login) {

        // LOGIN
        composable(route = Routes.Login) {
            val st = authVm.state.collectAsStateWithLifecycle().value
            LoginScreen(
                state = st,
                onEmailChange = authVm::onEmail,
                onPasswordChange = authVm::onPassword,
                onLogin = {
                    authVm.doLogin()
                    nav.navigate(Routes.Menu) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onRegisterClick = { nav.navigate(Routes.Register) }
            )
        }

        // REGISTER
        composable(route = Routes.Register) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate(Routes.Menu) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }

        // MENÚ
        composable(route = Routes.Menu) {
            val menuState = menuVm.state.collectAsStateWithLifecycle().value
            val cartState = cartVm.state.collectAsStateWithLifecycle().value
            val cartCount = cartState.items.sumOf { it.qty }

            MenuScreen(
                state = menuState,
                cartCount = cartCount,
                onQueryChange = menuVm::onQueryChange,
                onPlatoClick = { id -> nav.navigate("${Routes.Plate}/$id") },
                onAddToCart = { dishModel: DishModel -> cartVm.add(dishModel) },
                onOpenCart = { nav.navigate(Routes.Cart) }
            )
        }

        // DETALLE
        composable(
            route = "${Routes.Plate}/{id}",
            arguments = listOf(navArgument(name = "id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id == null) {
                nav.popBackStack()
                return@composable
            }

            // La pantalla de detalle también trabaja con DishModel
            PlatoDetailScreen(
                id = id,
                onBack = { nav.popBackStack() },
                onAddClick = { dishModel: DishModel ->
                    cartVm.add(dishModel)
                }
            )
        }

        // CARRITO
        composable(route = Routes.Cart) {
            val cartState = cartVm.state.collectAsStateWithLifecycle().value
            CartScreen(
                state = cartState,
                onRemove = { dishModel: DishModel -> cartVm.remove(dishModel) },
                onClear = { cartVm.clear() },
                onCheckout = { nav.navigate(Routes.Checkout) },
                onBack = { nav.popBackStack() }
            )
        }

        // CHECKOUT
        composable(route = Routes.Checkout) {
            val cartState = cartVm.state.collectAsStateWithLifecycle().value
            CheckoutScreen(
                state = cartState,
                onBack = { nav.popBackStack() },
                onConfirm = {
                    cartVm.clear()
                    nav.popBackStack(route = Routes.Menu, inclusive = false)
                }
            )
        }
    }
}
