package com.example.segunda_entrega.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.segunda_entrega.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {
    @Insert
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM user_table")
    fun getUsers(): Flow<List<Usuario>>

    @Query("SELECT * FROM user_table WHERE mail = :mail")
    suspend fun getUserByEmail(mail: String): Usuario?

    @Delete
    fun deleteUser(usuario: Usuario)

    @Query("DELETE FROM user_table")
    fun deleteAll()

    @Update
    fun updateUser(usuario: Usuario)
}