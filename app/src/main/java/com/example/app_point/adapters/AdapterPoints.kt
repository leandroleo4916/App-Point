package com.example.app_point.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsEntity
import com.example.app_point.model.ViewHolderPoints

class AdapterPoints(private val application: Application) : RecyclerView.Adapter<ViewHolderPoints>() {

    private var mListFullEmployee: ArrayList<PointsEntity?> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPoints {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_time_line, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.animation_left)
        item.startAnimation(animation)

        return ViewHolderPoints(item)
    }

    override fun onBindViewHolder(holder: ViewHolderPoints, position: Int) {

        val fullEmployee = mListFullEmployee[position]
        holder.bindData(fullEmployee?.data.toString())
        holder.bindHora(fullEmployee)
    }

    override fun getItemCount(): Int { return mListFullEmployee.count() }

    fun updateFullEmployee(list: ArrayList<PointsEntity?>){
        mListFullEmployee = when {
            list.size > 1 -> { list.reversed() as ArrayList<PointsEntity?> }
            else -> { list }
        }
        notifyDataSetChanged()
    }
}