package com.example.segunda_entrega

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.segunda_entrega.modelo.CrearProducto
import com.example.segunda_entrega.modelo.LlamadoProducto
import com.example.segunda_entrega.modelo.Usuario
import com.example.segunda_entrega.modelo.UsuarioLogin
import com.example.segunda_entrega.ui.theme.Segunda_entregaTheme
import com.example.segunda_entrega.util.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.segunda_entrega.dao.UsuarioDAO
import com.example.segunda_entrega.db.AppDatabase
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.layout.ContentScale

class MainViewModel : ViewModel() {
    val isLoading = mutableStateOf(false)
    private val _userCreationResult = MutableStateFlow<UserCreationResult?>(null)
    val userCreationResult = _userCreationResult.asStateFlow()
    private val _productos = MutableStateFlow<List<LlamadoProducto>?>(null)
    val productos = _productos.asStateFlow()
    private val _userName = MutableStateFlow<String?>(null)

    fun loginUsuario(mail: String, pass: String) {
        var usuario: UsuarioLogin
        usuario = UsuarioLogin(mail, pass)


        viewModelScope.launch {
            isLoading.value = true
            val response = RetrofitInstance.api.usuarioLogin(usuario)

            if (response.isSuccessful) {
                val respuesta = response.body()?.get(0)?.RESPUESTA
                _userCreationResult.value = UserCreationResult.Success(respuesta)

            } else {
                println("RESPUESTA ERROR")
                //_userCreationResult.value = UserCreationResult.Error(response.message())
            }
            isLoading.value = false
            resetUserCreationResult()
        }
    }
    /*
    fun createUser(mail: String, pass: String, nombre: String, apellido: String ) {
        var usuario: Usuario
        usuario = Usuario(mail, pass, nombre, apellido)

        viewModelScope.launch {
            isLoading.value = true
            val response = RetrofitInstance.api.usuarioAlmacenar(usuario)

            if (response.isSuccessful) {
                val respuesta = response.body()?.get(0)?.RESPUESTA
                _userCreationResult.value = UserCreationResult.Success(respuesta)

            } else {
                println("NOK")
                //_userCreationResult.value = UserCreationResult.Error(response.message())
            }
            isLoading.value = false
            resetUserCreationResult()
        }
    }*/
/*
    fun createProducto(codigo: String, nombre: String, descripcion: String, precio: Int, mail: String ) {
        var usuario: CrearProducto
        usuario = CrearProducto(codigo, nombre, descripcion, precio, mail)

        viewModelScope.launch {
            isLoading.value = true
            val response = RetrofitInstance.api.crearProducto(usuario)

            if (response.isSuccessful) {
                val respuesta = response.body()?.get(0)?.RESPUESTA
                _userCreationResult.value = UserCreationResult.Success(respuesta)

            } else {
                println("NOK")
                //_userCreationResult.value = UserCreationResult.Error(response.message())
            }
            isLoading.value = false
            resetUserCreationResult()
        }
    }*/
    /* listProducto() {

        viewModelScope.launch {
            isLoading.value = true
            val response = RetrofitInstance.api.getProductosPorCorreo()
            // val response = RetrofitInstance.api.getProductosPorCorreo1("admin@gmail.com")
            //val response = RetrofitInstance.api.getProductosPorCorreo2("admin@gmail.com")

            if (response.isSuccessful) {
                val respuesta = response.body()?.get(0)?.RESPUESTA
                _userCreationResult.value = UserCreationResult.Success(respuesta)

                print("frj $respuesta")
            } else {
                println("frj NO LISTA")
                //_userCreationResult.value = UserCreationResult.Error(response.message())
            }
            isLoading.value = false
            resetUserCreationResult()
        }
    }*/

    fun listProducto() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.getProductosPorCorreo()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    println("frj: Respuesta cruda: $responseBody")

                    // Deserialización manual usando Gson
                    val gson = Gson()
                    val listType = object : TypeToken<List<LlamadoProducto>>() {}.type

                    // Asumiendo que el primer elemento es la lista de productos
                    val jsonArray = gson.fromJson(responseBody, List::class.java)
                    val productosJson = gson.toJson(jsonArray[0])
                    val productos: List<LlamadoProducto> = gson.fromJson(productosJson, listType)

                    println("frj: Productos obtenidos correctamente: $productos")
                    productos.forEach { println("Producto: ${it.p_nombre}") }
                    _productos.value = productos
                } else {
                    println("frj: Error en la respuesta: ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                println("frj: Error HTTP: ${e.message}")
            } catch (e: IOException) {
                println("frj: Error de red: ${e.message}")
            } catch (e: Exception) {
                println("frj: Excepción al obtener productos: ${e.message}")
            }
            isLoading.value = false
        }
    }


fun resetUserCreationResult() {
        viewModelScope.launch {
            delay(1000)
            _userCreationResult.value = null
        }
    }

    sealed class UserCreationResult {
        data class Success(val data: Any?): UserCreationResult()
        data class Error(val message: String): UserCreationResult()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Segunda_entregaTheme {
                // Cambiar el color de la barra de estado
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color(0xFF9A5AFF) // Color lila exacto para la barra de estado
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEDE7F6) // Fondo lila claro
                ) {
                    MiAplicacion()
                }
            }
        }
    }
}

@Composable
fun MiAplicacion() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { PantallaLogin(navController, userViewModel) }
        composable("nuevo-usuario") { PantallaNuevoUsuario(navController, userViewModel) }
        composable("home") { PantallaHome(navController, userViewModel) }
        composable("agregar-productos") { AgregarProductos(navController, userViewModel) }
        composable("listar-productos") { ListaProductos(navController, userViewModel) }
        composable("admin") { PantallaAdministrador(navController) }
        composable("perfil-usuario") { PantallaPerfilUsuario(navController, userViewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(navController: NavController, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val scope = rememberCoroutineScope()

    Surface(
        color = Color(0xFFEDE7F6),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "¡Bienvenido a GameStation!",
                fontSize = 24.sp,
                color = Color(0xFF000000),
                modifier = Modifier.padding(bottom = 40.dp),
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password

                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    when {
                        email.isEmpty() || password.isEmpty() -> {
                            loginResult = "Ingrese su correo electrónico y contraseña."
                        }
                        else -> {
                            scope.launch {
                                val user = userDao.getUserByEmail(email)
                                if (user == null) {
                                    loginResult = "Credenciales Inválidas"
                                } else if (user.pass == password) {
                                    userViewModel.setUser(user) // Actualizar el ViewModel con los datos del usuario
                                    loginResult = if (email == "admin@gmail.com" && password == "admin") {
                                        "Administrador"
                                    } else {
                                        "Inicio correcto"
                                    }
                                } else {
                                    loginResult = "Contraseña incorrecta."
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("INICIAR SESIÓN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("¿No tienes cuenta?", color = Color(0xFF8A00FE))
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    navController.navigate("nuevo-usuario")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("REGISTRARSE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(60.dp))
            val painter: Painter = painterResource(id = R.drawable.logo)
            Image(
                painter = painter,
                contentDescription = "Game Controller",
                modifier = Modifier.size(200.dp)
            )

            loginResult?.let {
                Text(
                    text = it,
                    color = when (it) {
                        "Inicio correcto", "Administrador" -> Color.Green
                        else -> Color.Red
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                if (it == "Inicio correcto") {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                } else if (it == "Administrador") {
                    LaunchedEffect(Unit) {
                        navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNuevoUsuario(navController: NavController, userViewModel: UserViewModel) {
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val scope = rememberCoroutineScope()

    var userCreationResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userCreationResult) {
        userCreationResult?.let { result ->
            if (result == "OK") {
                Toast.makeText(context, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
                navController.navigate("login")
            } else {
                Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(
        color = Color(0xFFEDE7F6),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "GAMESTATION",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                modifier = Modifier.padding(bottom = 40.dp),
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(0.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(0.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(0.dp))
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Al hacer clic en \"Registrarte\", aceptas nuestras Condiciones, la Política de privacidad y la Política de cookies. Es posible que te enviemos notificaciones por SMS, que puedes desactivar cuando quieras.",
                fontSize = 14.sp,
                color = Color(0xFF000000),
                modifier = Modifier.padding(bottom = 30.dp),
                textAlign = TextAlign.Left
            )
            validationMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Button(
                onClick = {
                    if (mail.isBlank() || pass.isBlank() || nombre.isBlank() || apellido.isBlank()) {
                        validationMessage = "Todos los campos son obligatorios. Por favor, ingrese todos los datos."
                    } else {
                        scope.launch {
                            val existingUser = userDao.getUserByEmail(mail)
                            if (existingUser != null) {
                                validationMessage = "El correo ya ha sido usado por otro usuario."
                            } else {
                                try {
                                    val usuario = Usuario(mail = mail, pass = pass, nombre = nombre, apellido = apellido)
                                    userDao.insert(usuario)
                                    userCreationResult = "OK"
                                } catch (e: Exception) {
                                    userCreationResult = "Error"
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("REGISTRARTE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("VOLVER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
            val painter: Painter = painterResource(id = R.drawable.logo)
            Image(
                painter = painter,
                contentDescription = "Game Controller",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdministrador(navController: NavController) {
    var showDialog by remember { mutableStateOf(false)}
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val users by userDao.getUsers().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8A00FE),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Administrador")
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp, // Icono de salir
                            contentDescription = "Salir",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LazyColumn {
                    items(users) { user ->
                        UserCard(user = user, userDao)
                    }
                }
            }
        }
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Cerrar sesión") },
            text = { Text(text = "¿Desea cerrar su sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate("login") {
                            popUpTo("admin") { inclusive = true }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                ) {
                    Text("No")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: Usuario, userDao: UsuarioDAO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Nombre: ${user.nombre}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Apellido: ${user.apellido}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Correo: ${user.mail}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {
                userDao.deleteUser(user)
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navController: NavController, userViewModel: UserViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showCart by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val productos by userViewModel.productos.collectAsState()
    val cart = remember { mutableStateListOf<CrearProducto>() }

    LaunchedEffect(Unit) {
        userViewModel.cargarProductos(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondohome), // Reemplaza 'tu_fondo' con el id de tu imagen de fondo
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ajusta esto según tus necesidades (Crop, Fit, FillBounds, etc.)
        )

        Surface(
            color = Color(0x66EDE7F6), // Fondo lila claro con transparencia
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF8A00FE))
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { showMenu = !showMenu },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.user_icon), // Asegúrate de tener el ícono en res/drawable
                                    contentDescription = "Perfil de Usuario",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Perfil de Usuario") },
                                    onClick = {
                                        showMenu = false
                                        navController.navigate("perfil-usuario")
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Salir") },
                                    onClick = {
                                        showMenu = false
                                        showDialog = true
                                    }
                                )
                            }
                            Text(
                                text = "GameStation APP",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            IconButton(
                                onClick = {
                                    showCart = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.carro), // Asegúrate de tener el ícono en res/drawable
                                    contentDescription = "Carro de Compras",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = { navController.navigate("listar-productos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                    ) {
                        Text("REVISAR TUS PRODUCTOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                item {
                    Button(
                        onClick = { navController.navigate("agregar-productos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                    ) {
                        Text("AÑADIR PRODUCTOS NUEVOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                item {
                    Text(
                        text = "¡Bienvenido, ${user?.nombre ?: "Usuario"}!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8A00FE),
                        modifier = Modifier.padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }

                items(productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                cart.add(producto)
                                Toast.makeText(context, "Producto agregado al carro", Toast.LENGTH_SHORT).show()
                            },
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = producto.nombre,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8A00FE)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = producto.descripcion,
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Precio: ${producto.precio}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.juego), // Asegúrate de tener el ícono en res/drawable
                                contentDescription = "Imagen del producto",
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(start = 16.dp) // Ajusta el padding si es necesario
                            )
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Cerrar sesión") },
                    text = { Text(text = "¿Desea cerrar su sesión?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                userViewModel.setUser(null) // Limpiar el usuario actual
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                        ) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                        ) {
                            Text("No")
                        }
                    }
                )
            }

            if (showCart) {
                AlertDialog(
                    onDismissRequest = { showCart = false },
                    title = { Text(text = "Carrito de Compras") },
                    text = {
                        Column {
                            cart.forEach { producto ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = producto.nombre)
                                    Text(text = "Precio: ${producto.precio}")
                                }
                            }
                            Text(
                                text = "Total: ${cart.sumOf { it.precio }}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showCart = false
                                Toast.makeText(context, "Procesando compra", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                        ) {
                            Text("Comprar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showCart = false
                                cart.clear()
                                Toast.makeText(
                                    context,
                                    "Compra cancelada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                        ) {
                            Text("Cancelar Compra")
                        }
                    }
                )
            }
        }
    }
}




val imageResources = listOf(
    R.drawable.a001,
    R.drawable.a002,
    R.drawable.a003,
    R.drawable.a004,
    R.drawable.a005
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfilUsuario(navController: NavController, userViewModel: UserViewModel) {
    val user by userViewModel.user.collectAsState()
    var nombre by remember { mutableStateOf(user?.nombre ?: "") }
    var apellido by remember { mutableStateOf(user?.apellido ?: "") }
    var pass by remember { mutableStateOf(user?.pass ?: "") }
    var showMessage by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Surface(
        color = Color(0xFFEDE7F6), // Fondo lila claro
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Perfil de Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            Text(
                text = "Datos actuales",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color(0xFF8A00FE),
                    focusedBorderColor = Color(0xFF8A00FE),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF8A00FE),
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    userViewModel.updateUser(nombre, apellido, pass, context)
                    showMessage = true
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("MODIFICAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("VOLVER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (showMessage) {
                Toast.makeText(LocalContext.current, "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductos(navController: NavController, userViewModel: UserViewModel) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf<String?>(null) }
    var productCreationResult by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val productoDao = db.productoDao()
    val scope = rememberCoroutineScope()

    LaunchedEffect(productCreationResult) {
        productCreationResult?.let { result ->
            if (result == "OK") {
                Toast.makeText(context, "Producto creado correctamente", Toast.LENGTH_LONG).show()
                navController.navigate("home")
            } else {
                Toast.makeText(context, "Error al crear el producto", Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(color = Color(0xFFEDE7F6)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Registro de producto",
                    fontSize = 30.sp,
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(bottom = 60.dp),
                    textAlign = TextAlign.Center
                )
                validationMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF8A00FE),
                        focusedBorderColor = Color(0xFF8A00FE),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8A00FE),
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF8A00FE),
                        focusedBorderColor = Color(0xFF8A00FE),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8A00FE),
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF8A00FE),
                        focusedBorderColor = Color(0xFF8A00FE),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8A00FE),
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF8A00FE),
                        focusedBorderColor = Color(0xFF8A00FE),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8A00FE),
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = mail,
                    onValueChange = { mail = it },
                    label = { Text("Mail") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF8A00FE),
                        focusedBorderColor = Color(0xFF8A00FE),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF8A00FE),
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        if (codigo.isBlank() || nombre.isBlank() || descripcion.isBlank() || precio.isBlank() || mail.isBlank()) {
                            validationMessage = "Todos los campos son obligatorios. Por favor, ingrese todos los datos."
                        } else {
                            scope.launch {
                                val existingProduct = productoDao.getProductByCodigo(codigo)
                                if (existingProduct != null) {
                                    validationMessage = "El código del producto ya ha sido usado."
                                } else {
                                    try {
                                        val producto = CrearProducto(
                                            codigo = codigo,
                                            nombre = nombre,
                                            descripcion = descripcion,
                                            precio = precio.toInt(),
                                            mail = mail
                                        )
                                        productoDao.insert(producto)
                                        productCreationResult = "OK"
                                    } catch (e: Exception) {
                                        productCreationResult = "Error"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                ) {
                    Text("CREAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        navController.navigate("home")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
                ) {
                    Text("VOLVER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}



@Composable
fun ListaProductos(navController: NavController, userViewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val productoDao = db.productoDao()
    val productos by productoDao.getProducts().collectAsState(initial = emptyList())
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading.value = false
    }

    Surface(color = Color(0xFFEDE7F6)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF8A00FE))
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lista de Productos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (isLoading.value) {
                        item {
                            Text("Cargando...", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        }
                    } else {
                        if (productos.isNotEmpty()) {
                            items(productos) { producto ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = producto.nombre,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF8A00FE)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = producto.descripcion,
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Precio: ${producto.precio}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Botón de eliminación
                                        Button(
                                            onClick = {
                                                scope.launch {
                                                    productoDao.deleteProduct(producto)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                        ) {
                                            Text("Eliminar", color = Color.White)
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Text("No hay productos disponibles", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Button(
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(3.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A00FE))
            ) {
                Text("Volver", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun PantallaHomePreview() {
    Segunda_entregaTheme {
        val navController = rememberNavController()
        ListaProductos(navController = navController)
    }
}*/
