package com.joevieyra.basededatossqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_usuarios.view.*
import kotlinx.android.synthetic.main.item_usuarios.view.imageView
import kotlinx.android.synthetic.main.item_usuarios.view.nombre

class UsuariosAdapter(private val mContext: Context, private val listaUsuarios: List<Usuarios>) : ArrayAdapter<Usuarios>(mContext, 0, listaUsuarios) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_usuarios, parent, false)

        val usuarios = listaUsuarios[position]

        layout.nombre.text = usuarios.nombre
        layout.telefono.text = "${usuarios.telefono}"

        val imageUri = ImageController.getImageUri(mContext, usuarios.idUsuarios.toLong())

        layout.imageView.setImageURI(imageUri)

        return layout
    }

}