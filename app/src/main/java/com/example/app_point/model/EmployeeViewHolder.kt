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

    fun bindEmail(email: String){
        val textEmail = itemView.findViewById<TextView>(R.id.txt_email)
        textEmail.text = email
    }

    fun bindPhone(phone: String){
        val textPhone = itemView.findViewById<TextView>(R.id.txt_telefone)
        textPhone.text = phone
    }

    fun bindAdmission(admission: String){
        val textAdmission = itemView.findViewById<TextView>(R.id.txt_admissao)
        textAdmission.text = admission
    }

    fun bindHourInOne(hourInOne: String){
        val textHour1 = itemView.findViewById<TextView>(R.id.campo_horario1)
        textHour1.text = hourInOne
    }

    fun bindHourInTwo(hourInTwo: String){
        val textHour2 = itemView.findViewById<TextView>(R.id.campo_horario2)
        textHour2.text = hourInTwo
    }

    fun bindHourOutOne(hourOutOne: String){
        val textHour3 = itemView.findViewById<TextView>(R.id.campo_horario3)
        textHour3.text = hourOutOne
    }

    fun bindHourOutTwo(hourOutTwo: String){
        val textHour4 = itemView.findViewById<TextView>(R.id.campo_horario4)
        textHour4.text = hourOutTwo
    }
}