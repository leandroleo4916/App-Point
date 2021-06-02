package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.PointsEntity

@Suppress("UNREACHABLE_CODE")
class AdapterPoints(private val application: Application) : RecyclerView.Adapter<ViewHolderPoints>() {

    private var mListFullEmployee: ArrayList<PointsEntity> = arrayListOf()
    private val mBusiness: BusinessEmployee = BusinessEmployee(application)

    // Create the list of the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPoints {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_time_line, parent, false)

        // Add animation to item RecyclerView
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return ViewHolderPoints(item)
    }

    // Send to ViewHolder item of List
    override fun onBindViewHolder(holder: ViewHolderPoints, position: Int) {

        val fullEmployee = mListFullEmployee[position]
        holder.bindData(fullEmployee.data.toString())

        //val employee = mBusiness.consultDadosEmployee(mListEmployee)
        //val hours = employee!!.horario1

        holder.bindHora(fullEmployee, "")
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
    }

    // Function inverter list
    fun updateFullEmployee(list: ArrayList<PointsEntity>){
        mListFullEmployee = when {
            list.size > 1 -> { list.reversed() as ArrayList<PointsEntity> }
            else -> { list }
        }
        notifyDataSetChanged()
    }
}