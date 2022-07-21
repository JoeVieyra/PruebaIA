package com.joevieyra.basededatossqlite

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "usuarios")
class Usuarios(
    val nombre:String,
    val telefono: Int,
    val mail: String,
    val imagen: Int,
    @PrimaryKey(autoGenerate = true)
    var idUsuarios: Int = 0
) : Serializable