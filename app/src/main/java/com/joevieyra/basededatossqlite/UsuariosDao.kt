package com.joevieyra.basededatossqlite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsuariosDao {
    @Query("SELECT * FROM usuarios")
    fun getAll(): LiveData<List<Usuarios>>

    @Query("SELECT * FROM usuarios WHERE idUsuarios = :id")
    fun get(id: Int): LiveData<Usuarios>

    @Insert
    fun insertAll(vararg usuarios: Usuarios): List<Long>

    @Update
    fun update(usuarios: Usuarios)

    @Delete
    fun delete(usuarios: Usuarios)
}