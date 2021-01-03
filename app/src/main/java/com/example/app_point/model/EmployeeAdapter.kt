package com.example.app_point.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class EmployeeAdapter: RecyclerView.Adapter<EmployeeViewHolder>() {

    private var mListEmployee: List<String> = arrayListOf()
    private var mListOffice: List<String> = arrayListOf()
    private var mListEmail: List<String> = arrayListOf()
    private var mListPhone: List<String> = arrayListOf()
    private var mListAdmission: List<String> = arrayListOf()
    private var mListHour1: List<String> = arrayListOf()
    private var mListHour2: List<String> = arrayListOf()
    private var mListHour3: List<String> = arrayListOf()
    private var mListHour4: List<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.perfil_employee, parent, false)

        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bindEmployee(mListEmployee[position])
        holder.bindOffice(mListOffice[position])
        holder.bindEmail(mListEmail[position])
        holder.bindPhone(mListPhone[position])
        holder.bindAdmission(mListAdmission[position])
        holder.bindHourInOne(mListHour1[position])
        holder.bindHourInTwo(mListHour2[position])
        holder.bindHourOutOne(mListHour3[position])
        holder.bindHourOutTwo(mListHour4[position])
    }

    override fun getItemCount(): Int {
        return mListEmployee.count()
        return mListOffice.count()
        return mListEmail.count()
        return mListPhone.count()
        return mListAdmission.count()
        return mListHour1.count()
        return mListHour2.count()
        return mListHour3.count()
        return mListHour4.count()
    }

    fun updateEmployee(list: List<String>){
        mListEmployee = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateOffice(list: List<String>){
        mListOffice = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateEmail(list: List<String>){
        mListEmail = list.asReversed()
        notifyDataSetChanged()
    }
    fun updatePhone(list: List<String>){
        mListPhone = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateAdmission(list: List<String>){
        mListAdmission = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHour1(list: List<String>){
        mListHour1 = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHour2(list: List<String>){
        mListHour2 = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHour3(list: List<String>){
        mListHour3 = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHour4(list: List<String>){
        mListHour4 = list.asReversed()
        notifyDataSetChanged()
    }
}