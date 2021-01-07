package com.example.app_point.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class EmployeeAdapter: RecyclerView.Adapter<EmployeeViewHolder>() {

    private var mListEmployee: List<String> = arrayListOf()
    private var mListOffice: List<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.perfil_employee, parent, false)
        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bindEmployee(mListEmployee[position])
        holder.bindOffice(mListOffice[position])
    }

    override fun getItemCount(): Int {
        return mListEmployee.count()
        return mListOffice.count()
    }

    fun updateEmployee(list: List<String>){
        mListEmployee = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateOffice(list: List<String>){
        mListOffice = list.asReversed()
        notifyDataSetChanged()
    }
}