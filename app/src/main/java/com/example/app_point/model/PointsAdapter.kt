package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.utils.AddHours
import com.example.app_point.utils.ConverterPhoto

@Suppress("UNREACHABLE_CODE")
class PointsAdapter(private val application: Application) : RecyclerView.Adapter<PointsViewHolder>() {

    private var mListEmployee: List<String> = arrayListOf()
    private var mListData: List<String> = arrayListOf()
    private var mListHora: List<String> = arrayListOf()
    private val mBusiness: BusinessEmployee = BusinessEmployee(application)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    // Create the list of the layout
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
        holder.bind(mListEmployee[position])

        // Captures name employee and search photo
        val photo = mBusiness.consultPhoto(mListEmployee[position])
        val photoConvert = mConverterPhoto.converterToBitmap(photo!!)
        holder.bindPhoto(photoConvert)

        holder.bindData(mListData[position])

        val hoursToMinutes = mBusiness.consultHours(mListEmployee[position])!!
        holder.bindHora(mListHora[position], hoursToMinutes)
    }

    override fun getItemCount(): Int {
        return mListEmployee.count()
        return mListData.count()
        return mListHora.count()
    }

    // Function inverter list
    fun updateEmployee(list: List<String>){
        mListEmployee = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateData(list: List<String>){
        mListData = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHora(list: List<String>){
        mListHora = list.asReversed()
        notifyDataSetChanged()
    }
}