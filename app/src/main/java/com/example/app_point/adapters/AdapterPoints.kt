package com.example.app_point.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.PointsFullEntity

class AdapterPoints(private val application: Application) : RecyclerView.Adapter<AdapterPoints.ViewHolderPoints>() {

    private var mListFullEmployee: ArrayList<PointsFullEntity?> = arrayListOf()

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

    inner class ViewHolderPoints(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(data: String){
            itemView.findViewById<TextView>(R.id.text_date_time_line).text = data
        }

        fun bindHora(fullEmployee: PointsFullEntity?) {

            val textHora1 = itemView.findViewById<TextView>(R.id.text_hour_time_line)
            val textHora2 = itemView.findViewById<TextView>(R.id.text_hour_time_line2)
            val textHora3 = itemView.findViewById<TextView>(R.id.text_hour_time_line3)
            val textHora4 = itemView.findViewById<TextView>(R.id.text_hour_time_line4)

            textHora1.text = fullEmployee?.hora1 ?: "--:--"
            textHora2.text = fullEmployee?.hora2 ?: "--:--"
            textHora3.text = fullEmployee?.hora3 ?: "--:--"
            textHora4.text = fullEmployee?.hora4 ?: "--:--"
        }
    }

    override fun getItemCount(): Int { return mListFullEmployee.count() }

    fun updateFullEmployee(list: ArrayList<PointsFullEntity?>){
        mListFullEmployee = list
        notifyDataSetChanged()
    }
}