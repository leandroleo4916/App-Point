package com.example.app_point.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class PointsAdapter: RecyclerView.Adapter<PointsViewHolder>() {

    private var mListNameEmployee: List<String> = arrayListOf()
    private var mListHrs: List<String> = arrayListOf()
    private var mListDate: List<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.points_employee, parent, false
        )
        return PointsViewHolder(item)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        holder.bindNameEmployee(mListNameEmployee[position])
        holder.bindHrsCurrent(mListHrs[position])
        holder.bindDateCurrent(mListDate[position])
    }

    override fun getItemCount(): Int {
        return mListNameEmployee.count()
        return mListHrs.count()
        return mListDate.count()
    }

    fun updateEmployee(list: List<String>){
        mListNameEmployee = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHours(list: List<String>){
        mListHrs = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateDate(list: List<String>) {
        mListDate = list.asReversed()
        notifyDataSetChanged()
    }
}