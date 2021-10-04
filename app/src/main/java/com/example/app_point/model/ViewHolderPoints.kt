package com.example.app_point.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsEntity
import com.example.app_point.utils.ConverterHours

class ViewHolderPoints(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mConverterHours: ConverterHours = ConverterHours()

    fun bindData(data: String){
        val textData = itemView.findViewById<TextView>(R.id.text_date_time_line)
        textData.text = data
    }

    fun bindHora(fullEmployee: PointsEntity, hours: String){

        val textHora = itemView.findViewById<TextView>(R.id.text_hour_time_line)
        val textHora2 = itemView.findViewById<TextView>(R.id.text_hour_time_line2)
        val textHora3 = itemView.findViewById<TextView>(R.id.text_hour_time_line3)
        val textHora4 = itemView.findViewById<TextView>(R.id.text_hour_time_line4)

        //val imageTimeLine = itemView.findViewById<ImageView>(R.id.imageViewColorTimeLine)
        //val minutesCurrent = mConverterHours.converterHoursInMinutes(hoursCurrent.hora1.toString())
        //val minutes = mConverterHours.converterHoursInMinutes(hours)

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
                imageTimeLine.setImageResource(R.color.colorGreen)
            }
            minutesCurrent > minutes + 15 -> {
                imageTimeLine.setImageResource(R.color.colorRed)
            }
            else -> {
                imageTimeLine.setImageResource(R.color.colorYellow)
            }
        }*/
    }
}