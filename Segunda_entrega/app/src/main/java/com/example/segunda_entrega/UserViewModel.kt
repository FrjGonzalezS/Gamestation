package com.example.segunda_entrega

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segunda_entrega.db.AppDatabase
import com.example.segunda_entrega.modelo.CrearProducto
import com.example.segunda_entrega.modelo.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow<Usuario?>(null)
    val user: StateFlow<Usuario?> = _user

    private val _productos = MutableStateFlow<List<CrearProducto>>(emptyList())
    val productos: StateFlow<List<CrearProducto>> = _productos

    fun setUser(usuario: Usuario?) {
        _user.value = usuario
    }

    fun updateUser(nombre: String?, apellido: String?, pass: String?, context: Context) {
        val currentUser = _user.value
        if (currentUser != null) {
            val updatedUser = currentUser.copy(
                nombre = nombre ?: currentUser.nombre,
                apellido = apellido ?: currentUser.apellido,
                pass = pass ?: currentUser.pass
            )
            _user.value = updatedUser

            viewModelScope.launch {
                val db = AppDatabase.getDatabase(context)
                val userDao = db.userDao()
                userDao.updateUser(updatedUser)
            }
        }
    }

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val productoDao = db.productoDao()
            _productos.value = productoDao.getProducts().first()
        }
    }
}
