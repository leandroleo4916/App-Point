package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee

@Suppress("UNREACHABLE_CODE")
class AdapterPoints(private val application: Application) : RecyclerView.Adapter<ViewHolderPoints>() {

    private var mListEmployee: List<String> = arrayListOf()
    private var mListData: List<String> = arrayListOf()
    private var mListHora: List<String> = arrayListOf()
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

        holder.bindData(mListData[position])
        holder.bindHora(mListHora[position])
    }

    override fun getItemCount(): Int {
        return mListData.count()
        return mListHora.count()
    }

    // Function inverter list

    fun updateData(list: List<String>){
        mListData = list.reversed()
        notifyDataSetChanged()
    }
    fun updateHora(list: List<String>){
        mListHora = list.reversed()
        notifyDataSetChanged()
    }
}