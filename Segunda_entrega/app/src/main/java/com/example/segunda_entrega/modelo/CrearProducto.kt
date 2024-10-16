package com.example.segunda_entrega.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class CrearProducto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var codigo: String,
    var nombre: String,
    var descripcion: String,
    var precio: Int,
    var mail: String
)