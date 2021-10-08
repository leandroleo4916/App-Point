package com.example.app_point.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.PointsEntity
import com.example.app_point.model.PointsViewHolder
import com.example.app_point.utils.ConverterPhoto

class PointsAdapter(private val application: Application, private val mBusiness: BusinessEmployee)
    : RecyclerView.Adapter<PointsViewHolder>() {

    private var mListFullEmployee: ArrayList<PointsEntity> = arrayListOf()
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)

        // Add animation to item RecyclerView
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return PointsViewHolder(item)
    }

    // Send to ViewHolder item of List
    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        //val employee = mListEmployee[position]
        val fullEmployee = mListFullEmployee[position]

        holder.bind(fullEmployee.employee.toString())
        holder.bindData(fullEmployee.data.toString())

        // Captures name employee and search photo
        val photo = mBusiness.consultPhoto(fullEmployee.employee.toString())
        val photoConvert = photo?.let { mConverterPhoto.converterToBitmap(it) }
        photoConvert?.let { holder.bindPhoto(it) }

        val horarioEmployee = mBusiness.consultHours(fullEmployee.employee.toString())!!
        holder.bindHora(fullEmployee)
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
    }

    fun updateFullEmployee(list: ArrayList<PointsEntity>){
        mListFullEmployee = when {
            list.size > 1 -> { list.reversed() as ArrayList<PointsEntity> }
            else -> { list }
        }
        notifyDataSetChanged()
    }
}