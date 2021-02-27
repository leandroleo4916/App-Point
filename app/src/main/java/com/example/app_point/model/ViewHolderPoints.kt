package com.example.app_point.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.utils.ConverterHours

class ViewHolderPoints(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mConverterHours: ConverterHours = ConverterHours()

    // Add item the position of the Layout
    fun bindData(data: String){
        val textData = itemView.findViewById<TextView>(R.id.text_date_time_line)
        textData.text = data
    }

    fun bindHora(hoursCurrent: String, hours: String){

        val textHora = itemView.findViewById<TextView>(R.id.text_hour_time_line)
        val imageTimeLine = itemView.findViewById<ImageView>(R.id.imageViewColorTimeLine)
        
        val minutesCurrent = mConverterHours.converterHoursInMinutes(hoursCurrent)
        val minutes = mConverterHours.converterHoursInMinutes(hours)

        textHora.text = hoursCurrent

        when {
            minutesCurrent < minutes -> {
                imageTimeLine.setImageResource(R.color.colorGreen)
            }
            minutesCurrent > minutes + 15 -> {
                imageTimeLine.setImageResource(R.color.colorRed)
            }
            else -> {
                imageTimeLine.setImageResource(R.color.colorYellow)
            }
        }
    }
}