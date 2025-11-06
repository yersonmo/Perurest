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

// Si usas un ServiceLocator/factory para Auth:
import com.perurest.di.ServiceLocator

// === Tipos: dominio vs modelo (evita el choque de nombres) ===
import com.perurest.domain.Dish as DishDomain
import com.perurest.domain.model.Dish as DishModel

/* --------------------------------------------------------------------------
   Conversión de dominio -> modelo.
   Si ya tienes un mapper oficial (p.ej. DishDomain.toModel()), úsalo y borra 'adapt'.
----------------------------------------------------------------------------*/
private fun adapt(d: DishDomain): DishModel =
    DishModel(
        id = d.id,
        name = d.name,
        description = d.description,
        price = d.price,
        imageUrl = d.imageUrl
    )

@Composable
fun PerurestNavHost() {
    val nav = rememberNavController()

    // ViewModels compartidos en el NavHost
    val authVm: AuthViewModel = viewModel(factory = ServiceLocator.authVmFactory())
    val menuVm: MenuViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    NavHost(navController = nav, startDestination = "login") {

        // -------------------- LOGIN --------------------
        composable(route = "login") {
            val st = authVm.state.collectAsStateWithLifecycle().value

            LoginScreen(
                state = st,
                onEmailChange = authVm::onEmail,
                onPasswordChange = authVm::onPassword,
                onLogin = {
                    authVm.doLogin()
                    nav.navigate("menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { nav.navigate("register") }
            )
        }

        // ------------------- REGISTER ------------------
        composable(route = "register") {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate("menu") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // --------------------- MENÚ --------------------
        composable(route = "menu") {
            val menuState = menuVm.state.collectAsStateWithLifecycle().value
            val cartState = cartVm.state.collectAsStateWithLifecycle().value
            val cartCount = cartState.items.sumOf { it.qty }

            MenuScreen(
                state = menuState,
                cartCount = cartCount,
                onQueryChange = menuVm::onQueryChange,
                onPlatoClick = { id -> nav.navigate("plate/$id") },
                onAddToCart = { dishModel: DishModel -> cartVm.add(dishModel) },   // <— DishModel aquí
                onOpenCart = { nav.navigate("cart") }
            )
        }

// DETALLE
        composable(
            route = "plate/{id}",
            arguments = listOf(navArgument(name = "id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            PlatoDetailScreen(
                id = id,
                onBack = { nav.popBackStack() },
                onAddClick = { dishDomain: DishDomain ->
                    // usa tu mapper real si lo tienes, p. ej. dishDomain.toModel()
                    val dishModel: DishModel = adapt(dishDomain)
                    cartVm.add(dishModel)
                }
            )
        }

// CARRITO
        composable(route = "cart") {
            val cartState = cartVm.state.collectAsStateWithLifecycle().value

            CartScreen(
                state = cartState,
                onRemove = { dishModel: DishModel -> cartVm.remove(dishModel) },   // <— DishModel aquí
                onClear = { cartVm.clear() },
                onCheckout = { nav.navigate("checkout") },
                onBack = { nav.popBackStack() }
            )
        }

// CHECKOUT
        composable(route = "checkout") {
            val cartState = cartVm.state.collectAsStateWithLifecycle().value

            CheckoutScreen(
                state = cartState,                 // <— nombre correcto del parámetro
                onBack = { nav.popBackStack() },
                onConfirm = {
                    cartVm.clear()
                    nav.popBackStack(route = "menu", inclusive = false)
                }
            )
        }
