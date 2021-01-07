package com.example.app_point.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class EmployeeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindEmployee(employee: String){
        val textEmployee = itemView.findViewById<TextView>(R.id.txt_nome)
        textEmployee.text = employee
    }

    fun bindOffice(office: String){
        val textOffice = itemView.findViewById<TextView>(R.id.txt_cargo)
        textOffice.text = office
    }
}