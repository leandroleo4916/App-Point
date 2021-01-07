package com.example.app_point.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class PointsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindNameEmployee(nameEmployee: String){
        val textNameEmployee = itemView.findViewById<TextView>(R.id.txt_nome)
        textNameEmployee.text = nameEmployee
    }
    fun bindHrsCurrent(hrsCurrent: String){
        val textHrsCurrent = itemView.findViewById<TextView>(R.id.txt_cargo)
        textHrsCurrent.text = hrsCurrent
    }
    fun bindDateCurrent(dateCurrent: String){
        val textDateCurrent = itemView.findViewById<TextView>(R.id.txt_cargo)
        textDateCurrent.text = dateCurrent
    }

}