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

class PointsAdapter(private val application: Application, private val mBusiness: BusinessEmployee) :
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
        val photo = mBusiness.consultPhoto(fullEmployee?.employee.toString())
        val photoConvert = photo?.let { converterPhoto.converterToBitmap(it) }
        photoConvert?.let { holder.bindPhoto(it) }

        holder.bind(fullEmployee?.employee.toString())
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

    fun updateNameEmployee(name: String) {

        for (employee in listFullEmployee) {
            if (employee!!.employee!!.contains(name)) {
                listNameEmployee.add(employee)
                listFullEmployee = listNameEmployee
            }
        }
        notifyDataSetChanged()
    }
}