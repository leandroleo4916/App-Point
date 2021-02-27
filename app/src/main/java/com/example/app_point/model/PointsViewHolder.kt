package com.example.app_point.model

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.utils.ConverterHours

class PointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mAddHours: ConverterHours = ConverterHours()

    // Add item the position of the Layout
    fun bind(employee: String){
        val textEmployee = itemView.findViewById<TextView>(R.id.text_nome_funcionario)
        textEmployee.text = employee
    }

    fun bindData(data: String){
        val textData = itemView.findViewById<TextView>(R.id.text_data)
        textData.text = data
    }

    fun bindHora(hoursCurrent: String, hours: String){
        val textHora = itemView.findViewById<TextView>(R.id.text_hora)
        val imageBack = itemView.findViewById<ImageView>(R.id.icon_image_back)
        
        val minutesCurrent = mAddHours.converterHoursInMinutes(hoursCurrent)
        val minutes = mAddHours.converterHoursInMinutes(hours)

        textHora.text = hoursCurrent

        when {
            minutesCurrent < minutes -> {
                imageBack.setImageResource(R.color.colorGreen)
            }
            minutesCurrent > minutes + 15 -> {
                imageBack.setImageResource(R.color.colorRed)
            }
            else -> {
                imageBack.setImageResource(R.color.colorYellow)
            }
        }
    }

    fun bindPhoto(image: Bitmap){
        val imageView = itemView.findViewById<ImageView>(R.id.icon_image)
        imageView.setImageBitmap(image)

    }

}