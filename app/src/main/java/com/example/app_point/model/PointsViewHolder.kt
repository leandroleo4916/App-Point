package com.example.app_point.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class PointsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindNameEmployee(nameEmployee: String){
        val textNameEmployee = itemView.findViewById<TextView>(R.id.text_name_employee)
        textNameEmployee.text = nameEmployee
    }
    fun bindHrsCurrent(hrsCurrent: String){
        val textHrsCurrent = itemView.findViewById<TextView>(R.id.text_hors_point)
        textHrsCurrent.text = hrsCurrent
    }
    fun bindDateCurrent(dateCurrent: String){
        val textDateCurrent = itemView.findViewById<TextView>(R.id.text_date_point)
        textDateCurrent.text = dateCurrent
    }

}