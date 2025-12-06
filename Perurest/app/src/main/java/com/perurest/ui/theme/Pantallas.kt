package com.perurest.ui.theme

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import java.text.NumberFormat
import java.util.Locale
import com.perurest.model.DetalleCarrito
import com.perurest.model.Plato
import com.perurest.model.Usuario
import com.perurest.viewmodel.ComidaViewModel

// Utilidad para moneda
fun formatoCLP(monto: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return format.format(monto)
}

// --- Pantalla Principal (Scaffold con Navegación) ---
@Composable
fun AppComidaPeruana(viewModel: ComidaViewModel, onExitApp: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val cantidadCarrito by viewModel.cantidadProductos.collectAsState()

    Scaffold(
        bottomBar = {
            if (currentRoute != "splash") {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                        label = { Text("Buscar") },
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") }
                    )
                    NavigationBarItem(
                        icon = {
                            BadgedBox(badge = {
                                if (cantidadCarrito > 0) Badge { Text("$cantidadCarrito") }
                            }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            }
                        },
                        label = { Text("Carrito") },
                        selected = currentRoute == "carrito",
                        onClick = { navController.navigate("carrito") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
                        label = { Text("Usuario") },
                        selected = currentRoute == "usuario",
                        onClick = { navController.navigate("usuario") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                PantallaBienvenida(onTimeout = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("home") {
                PantallaListaPlatos(viewModel) { platoId ->
                    navController.navigate("detalle/$platoId")
                }
            }
            composable("detalle/{platoId}") { backStackEntry ->
                val platoId = backStackEntry.arguments?.getString("platoId")?.toIntOrNull()
                PantallaDetallePlato(platoId, viewModel)
            }
            composable("carrito") {
                PantallaCarrito(viewModel)
            }
            composable("usuario") {
                PantallaUsuario(viewModel)
            }
        }
    }

    if (currentRoute == "home") {
        BackHandler { onExitApp() }
    }
}

// --- Pantallas Básicas ---
@Composable
fun PantallaBienvenida(onTimeout: () -> Unit) {
    LaunchedEffect(true) {
        kotlinx.coroutines.delay(2000)
        onTimeout()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFD32F2F), Color(0xFFEF5350)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.White)
            Text("Sabores del Perú", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PantallaListaPlatos(viewModel: ComidaViewModel, onPlatoClick: (Int) -> Unit) {
    val platos by viewModel.listaPlatos.collectAsState()
    val busqueda by viewModel.consultaBusqueda.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { viewModel.actualizarBusqueda(it) },
            label = { Text("Buscar plato...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(platos) { plato ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onPlatoClick(plato.id) }, elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fastfood, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color(0xFFD32F2F))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(plato.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(formatoCLP(plato.precio), color = Color(0xFF388E3C))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaDetallePlato(platoId: Int?, viewModel: ComidaViewModel) {
    val platos by viewModel.listaPlatos.collectAsState()
    val plato = platos.find { it.id == platoId }
    val context = LocalContext.current

    if (plato != null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Gray)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(plato.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(formatoCLP(plato.precio), style = MaterialTheme.typography.headlineSmall, color = Color(0xFF388E3C))
            Spacer(modifier = Modifier.height(16.dp))
            Text(plato.descripcion)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    viewModel.agregarAlCarrito(
                        platoId = plato.id,
                        onNoRegistrado = { Toast.makeText(context, "Debe estar registrado para agregar productos al carrito de compras", Toast.LENGTH_LONG).show() },
                        onExito = { Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show() }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("AGREGAR") }
        }
    }
}

@Composable
fun PantallaCarrito(viewModel: ComidaViewModel) {
    val carrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalCarrito.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tu Pedido", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(carrito) { detalle ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(detalle.plato.nombre, fontWeight = FontWeight.Bold)
                        Text("${detalle.cantidad} x ${formatoCLP(detalle.plato.precio)}")
                    }
                    Text(formatoCLP(detalle.plato.precio * detalle.cantidad), fontWeight = FontWeight.Bold)
                    IconButton(onClick = { viewModel.eliminarDelCarrito(detalle) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
                Divider()
            }
        }
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total:", style = MaterialTheme.typography.titleLarge)
                Text(formatoCLP(total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Pantalla de Usuario (Login / Registro / Perfil) ---
@Composable
fun PantallaUsuario(viewModel: ComidaViewModel) {
    val usuario by viewModel.usuarioActivo.collectAsState()
    val context = LocalContext.current

    // Estado para saber si estamos en modo Login o Registro cuando no hay usuario
    var esModoLogin by remember { mutableStateOf(true) }

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(usuario) {
        usuario?.let {
            nombre = it.nombre
            apellido = it.apellido
            direccion = it.direccion
            ciudad = it.ciudad
            correo = it.correo
            telefono = it.telefono
        } ?: run {
            // Limpiar si se cierra sesión
            if (esModoLogin) {
                // Mantener correo quizas? No, limpiemos todo para seguridad
                // nombre = ""; apellido = ""; ...
            }
        }
    }

    if (usuario == null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Tabs superiores
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Button(
                    onClick = { esModoLogin = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = if (esModoLogin) MaterialTheme.colorScheme.primary else Color.Gray)
                ) { Text("Iniciar Sesión") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { esModoLogin = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = if (!esModoLogin) MaterialTheme.colorScheme.primary else Color.Gray)
                ) { Text("Registrarse") }
            }

            if (esModoLogin) {
                // Formulario Login
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bienvenido de nuevo", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            if (correo.isNotEmpty()) {
                                viewModel.iniciarSesion(
                                    correo = correo,
                                    onError = { Toast.makeText(context, "Usuario no registrado, debe registrarse", Toast.LENGTH_LONG).show() },
                                    onSuccess = { Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show() }
                                )
                            } else {
                                Toast.makeText(context, "Ingrese su correo", Toast.LENGTH_SHORT).show()
                            }
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text("INGRESAR")
                        }
                    }
                }
            } else {
                // Formulario Registro
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Crear cuenta nueva", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {
                        if (nombre.isNotEmpty() && correo.isNotEmpty()) {
                            viewModel.registrarUsuario(Usuario(nombre = nombre, apellido = apellido, direccion = direccion, ciudad = ciudad, correo = correo, telefono = telefono))
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("REGISTRARME")
                    }
                }
            }
        }
    } else {
        // Vista de Perfil (Logueado)
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hola, ${usuario?.nombre}", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(24.dp))

            Text("Mis Datos:", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, readOnly = true, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Modificar lógica simple: Re-registramos con el mismo ID
                viewModel.registrarUsuario(Usuario(id = usuario!!.id, nombre = nombre, apellido = apellido, direccion = direccion, ciudad = ciudad, correo = correo, telefono = telefono))
                Toast.makeText(context, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("MODIFICAR DATOS")
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { viewModel.cerrarSesion() }, modifier = Modifier.fillMaxWidth()) {
                Text("CERRAR SESIÓN")
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { mostrarDialogoEliminar = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ELIMINAR USUARIO")
            }
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Está seguro de eliminar usuario?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarUsuarioActual()
                    mostrarDialogoEliminar = false
                }) { Text("Sí, eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) { Text("Cancelar") }
            }
        )
    }
}