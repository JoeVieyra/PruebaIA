package com.joevieyra.basededatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_usuarios.*


class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var usuarios: Usuarios
    private lateinit var usuariosLiveData: LiveData<Usuarios>
    private val EDIT_ACTIVITY = 49

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       var listaUsuarios = emptyList<Usuarios>()

        val database = AppDatabase.getDatabase(this)

        database.usuarios().getAll().observe(this, Observer {
            listaUsuarios = it

            val adapter = UsuariosAdapter(this, listaUsuarios )

            lista.adapter = adapter
        })

        lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, UsuariosActivity::class.java)
            intent.putExtra("id", listaUsuarios[position].idUsuarios)
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, NuevoUsuarioActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_item -> {
                val intent = Intent(this, NuevoUsuarioActivity::class.java)
                intent.putExtra("usuarios", usuarios)
                startActivityForResult(intent, EDIT_ACTIVITY)
            }

            R.id.delete_item -> {
                usuariosLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.usuarios().delete(usuarios)
                    ImageController.deleteImage(this@MainActivity, usuarios.idUsuarios.toLong())
                    this@MainActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}