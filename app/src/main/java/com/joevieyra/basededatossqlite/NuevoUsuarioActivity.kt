package com.joevieyra.basededatossqlite

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_nuevo_usuarios.*
import kotlinx.android.synthetic.main.activity_nuevo_usuarios.imageSelect_iv
import kotlinx.android.synthetic.main.activity_nuevo_usuarios.nombre_et
import kotlinx.android.synthetic.main.activity_nuevo_usuarios.save_btn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NuevoUsuarioActivity : AppCompatActivity() {

    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_usuarios)

        var idUsuarios: Int? = null

        if (intent.hasExtra("usuarios")) {
            val usuarios = intent.extras?.getSerializable("usuarios") as Usuarios

            nombre_et.setText(usuarios.nombre)
            telefono_et.setText(usuarios.telefono.toString())
            mail_et.setText(usuarios.mail)
            idUsuarios = usuarios.idUsuarios

            val imageUri = ImageController.getImageUri(this, idUsuarios.toLong())
            imageSelect_iv.setImageURI(imageUri)
        }

        val database = AppDatabase.getDatabase(this)

        save_btn.setOnClickListener {
            val nombre = nombre_et.text.toString()
            val telefono = telefono_et.text.toString().toInt()
            val mail = mail_et.text.toString()

            val usuarios = Usuarios(nombre, telefono, mail, R.drawable.ic_launcher_background)

            if (idUsuarios != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    usuarios.idUsuarios = idUsuarios

                    database.usuarios().update(usuarios)

                    imageUri?.let {
                        val intent = Intent()
                        intent.data = it
                        setResult(Activity.RESULT_OK, intent)
                        ImageController.saveImage(this@NuevoUsuarioActivity, idUsuarios.toLong(), it)
                    }

                    this@NuevoUsuarioActivity.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = database.usuarios().insertAll(usuarios)[0]

                    imageUri?.let {
                        ImageController.saveImage(this@NuevoUsuarioActivity, id, it)
                    }

                    this@NuevoUsuarioActivity.finish()
                }
            }
        }

        imageSelect_iv.setOnClickListener {
            ImageController.selectPhotoFromGallery(this, SELECT_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data

                imageSelect_iv.setImageURI(imageUri)
            }
        }
    }
}