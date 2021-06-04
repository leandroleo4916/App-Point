package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.utils.ConverterPhoto

@Suppress("UNREACHABLE_CODE")
class EmployeeAdapter(private val application: Application) : RecyclerView.Adapter<EmployeeViewHolder>() {

    private var mListFullEmployee: ArrayList<EmployeeEntity> = arrayListOf()
    //private var mListFullPoints: ArrayList<PointsEntity> = arrayListOf()
    private val mBusiness: BusinessEmployee = BusinessEmployee(application)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    // Create the list of the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        // Add animation to item RecyclerView
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return EmployeeViewHolder(item)
    }

    // Send to ViewHolder item of List
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        //val employee = mListEmployee[position]
        //val fullPoints = mListFullPoints[position]
        val fullEmployee = mListFullEmployee[position]

        holder.bind(fullEmployee.nameEmployee, fullEmployee.cargoEmployee, fullEmployee.admissaoEmployee)
        //holder.bindData(fullPoints.data.toString())

        val photoConvert = fullEmployee.photo.let { mConverterPhoto.converterToBitmap(it) }
        photoConvert.let { holder.bindPhoto(it) }

        //val horarioEmployee = mBusiness.consultHours(fullPoints.employee.toString())!!
        //holder.bindHora(fullPoints, horarioEmployee)
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
        //return mListFullPoints.count()
    }

    // Function inverter list
    /*fun updateFullPoints(list: ArrayList<PointsEntity>){
        mListFullPoints = list
        notifyDataSetChanged()
    }*/
    fun updateFullEmployee(list: ArrayList<EmployeeEntity>){
        mListFullEmployee = list
        notifyDataSetChanged()
    }
}