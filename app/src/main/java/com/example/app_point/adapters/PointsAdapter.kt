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
import com.example.app_point.viewholver.PointsViewHolder
import com.example.app_point.utils.ConverterPhoto

class PointsAdapter (private val application: Application, private val mBusiness: BusinessEmployee) :
    RecyclerView.Adapter<PointsViewHolder>() {

    private var listFullEmployee: ArrayList<PointsEntity?> = arrayListOf()
    private var listNameEmployee: ArrayList<PointsEntity?> = arrayListOf()
    private val converterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation(application, R.anim.animation_left)
        item.startAnimation(animation)

        return PointsViewHolder(item)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {

        val fullEmployee = listFullEmployee[position]

        val photo = fullEmployee?.idEmployee?.let { mBusiness.consultPhoto(it) }
        val name = fullEmployee?.idEmployee?.let { mBusiness.consultNameEmployeeById(it) }
        val photoConvert = photo?.let { converterPhoto.converterToBitmap(it) }
        photoConvert?.let { holder.bindPhoto(it) }

        name?.let { holder.bind(it) }
        holder.bindData(fullEmployee?.data.toString())
        holder.bindHora(fullEmployee)
    }

    override fun getItemCount(): Int {
        return listFullEmployee.count()
    }

    fun updateFullPoints(list: ArrayList<PointsEntity?>) {
        val listReverse = list.reversed()
        listFullEmployee = when {
            listReverse.size > 1 -> listReverse as ArrayList<PointsEntity?>
            else -> list
        }
        notifyDataSetChanged()
    }
}