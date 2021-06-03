package com.example.app_point.model

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsEntity
import com.example.app_point.utils.ConverterHours

class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    fun bindHora(fullEmployee: PointsEntity, horarioEmployee: List<String> ){

        val textHora = itemView.findViewById<TextView>(R.id.text_hora)
        val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
        val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
        val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)
        val imageBack = itemView.findViewById<ImageView>(R.id.icon_image_back)

        //val minutesCurrent = mAddHours.converterHoursInMinutes(hoursCurrent)
        //val minutes = mAddHours.converterHoursInMinutes(hours)

        when {
            fullEmployee.hora1 != null && fullEmployee.hora2 == null -> {
                textHora.text = fullEmployee.hora1
                textHora2.text = "--:--"
                textHora3.text = "--:--"
                textHora4.text = "--:--"
            }
            fullEmployee.hora1 != null && fullEmployee.hora2 != null && fullEmployee.hora3 == null -> {
                textHora.text = fullEmployee.hora1
                textHora2.text = fullEmployee.hora2
                textHora3.text = "--:--"
                textHora4.text = "--:--"
            }
            fullEmployee.hora1 != null && fullEmployee.hora2 != null && fullEmployee.hora3 != null && fullEmployee.hora4 == null -> {
                textHora.text = fullEmployee.hora1
                textHora2.text = fullEmployee.hora2
                textHora3.text = fullEmployee.hora3
                textHora4.text = "--:--"
            }
            else -> {
                textHora.text = fullEmployee.hora1
                textHora2.text = fullEmployee.hora2
                textHora3.text = fullEmployee.hora3
                textHora4.text = fullEmployee.hora4
            }
        }

        /*when {
            minutesCurrent < minutes -> {
                imageBack.setImageResource(R.color.colorGreen)
            }
            minutesCurrent > minutes + 15 -> {
                imageBack.setImageResource(R.color.colorRed)
            }
            else -> {
                imageBack.setImageResource(R.color.colorYellow)
            }
        }*/
    }

    fun bindPhoto(image: Bitmap){
        val imageView = itemView.findViewById<ImageView>(R.id.icon_image)
        imageView.setImageBitmap(image)

    }
}