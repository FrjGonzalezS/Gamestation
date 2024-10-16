package com.example.segunda_entrega.modelo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var mail: String,
    var pass: String,
    var nombre: String,
    var apellido: String
)