package com.joevieyra.basededatossqlite

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_usuarios.*
import kotlinx.android.synthetic.main.activity_usuarios.imagen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuariosActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var usuarios: Usuarios
    private lateinit var usuariosLiveData: LiveData<Usuarios>
    private val EDIT_ACTIVITY = 49

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        database = AppDatabase.getDatabase(this)

        val idUsuarios = intent.getIntExtra("id", 0)

        val imageUri = ImageController.getImageUri(this, idUsuarios.toLong())
        imagen.setImageURI(imageUri)

        usuariosLiveData = database.usuarios().get(idUsuarios)

        usuariosLiveData.observe(this, Observer {
            usuarios = it


            nombre_usuarios.text = usuarios.nombre
            telefono_usuarios.text = "${usuarios.telefono}"
            mail_usuarios.text = usuarios.mail
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.usuarios_menu, menu)

        return super.onCreateOptionsMenu(menu)
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
                    ImageController.deleteImage(this@UsuariosActivity, usuarios.idUsuarios.toLong())
                    this@UsuariosActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == EDIT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imagen.setImageURI(data!!.data)
            }
        }
    }
}
