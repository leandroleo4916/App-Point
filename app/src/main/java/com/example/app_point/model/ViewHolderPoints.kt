package com.example.app_point.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsEntity

class ViewHolderPoints(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(data: String){
        itemView.findViewById<TextView>(R.id.text_date_time_line).text = data
    }

    fun bindHora(fullEmployee: PointsEntity?) {

        val textHora1 = itemView.findViewById<TextView>(R.id.text_hour_time_line)
        val textHora2 = itemView.findViewById<TextView>(R.id.text_hour_time_line2)
        val textHora3 = itemView.findViewById<TextView>(R.id.text_hour_time_line3)
        val textHora4 = itemView.findViewById<TextView>(R.id.text_hour_time_line4)

        textHora1.text = fullEmployee?.hora1 ?: "--:--"
        textHora2.text = fullEmployee?.hora2 ?: "--:--"
        textHora3.text = fullEmployee?.hora3 ?: "--:--"
        textHora4.text = fullEmployee?.hora4 ?: "--:--"
    }
}