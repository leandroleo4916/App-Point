package com.example.app_point.model

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsEntity
import com.example.app_point.utils.ConverterHours

class PointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(employee: String){
        itemView.findViewById<TextView>(R.id.text_view_name_employee).text = employee
    }

    fun bindData (data: String){ itemView.findViewById<TextView>(R.id.text_data).text = data }

    fun bindHora(fullEmployee: PointsEntity?){

        val textHora1 = itemView.findViewById<TextView>(R.id.text_hora)
        val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
        val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
        val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)

        textHora1.text = fullEmployee?.hora1
        textHora2.text = fullEmployee?.hora2
        textHora3.text = fullEmployee?.hora3
        textHora4.text = fullEmployee?.hora4
    }

    fun bindPhoto(image: Bitmap){
        itemView.findViewById<ImageView>(R.id.icon_image_perfil).setImageBitmap(image)
    }
}