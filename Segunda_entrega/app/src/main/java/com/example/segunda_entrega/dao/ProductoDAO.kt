package com.example.segunda_entrega.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.segunda_entrega.modelo.CrearProducto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDAO {
    @Insert
    suspend fun insert(producto: CrearProducto)

    @Query("SELECT * FROM product_table WHERE codigo = :codigo")
    suspend fun getProductByCodigo(codigo: String): CrearProducto?

    @Query("SELECT * FROM product_table")
    fun getProducts(): Flow<List<CrearProducto>>

    @Delete
    suspend fun deleteProduct(producto: CrearProducto)

    @Query("DELETE FROM product_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM product_table")
    suspend fun getProductsList(): List<CrearProducto>
}

