package com.example.app_point.activitys

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class PontosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(funcionario: String){
        val textFuncionario = itemView.findViewById<TextView>(R.id.text_nome_funcionario)
        textFuncionario.text = funcionario
    }

    fun bindData(data: String){
        val textData = itemView.findViewById<TextView>(R.id.text_data)
        textData.text = data
    }

    fun bindHora(hora: String){
        val textHora = itemView.findViewById<TextView>(R.id.text_hora)
        textHora.text = hora
    }

    fun bindPhoto(image: Bitmap){
        val imageView = itemView.findViewById<ImageView>(R.id.icon_image)
        imageView.setImageBitmap(image)

    }

}