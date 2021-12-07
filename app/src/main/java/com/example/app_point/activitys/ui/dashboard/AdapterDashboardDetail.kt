package com.example.app_point.activitys.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EntityDashboard
import com.example.app_point.utils.ConverterPhoto

class AdapterDashboardDetail (private val converterPhoto: ConverterPhoto) :
    RecyclerView.Adapter<AdapterDashboardDetail.ViewHolderDetail>() {

    private var listEmployeeHourExtra: ArrayList<EntityDashboard> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDetail {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_detail_employee, parent, false)

        return ViewHolderDetail(item)
    }

    override fun onBindViewHolder(holder: ViewHolderDetail, position: Int) {

        val fullEmployee = listEmployeeHourExtra[position]
        holder.bindPhotoAndHour(fullEmployee)

    }

    inner class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemPhoto: ImageView = itemView.findViewById(R.id.text_employee_home)
        private val itemHoursExtras: TextView = itemView.findViewById(R.id.text_hours_extras)
        private val itemHoursDone: TextView = itemView.findViewById(R.id.text_hours_feitas)

        init { itemPhoto.setOnClickListener(this) }

        fun bindPhotoAndHour(employee: EntityDashboard){
            val photo = converterPhoto.converterToBitmap(employee.photo)
            itemPhoto.setImageBitmap(photo)
            itemHoursExtras.text = employee.extraHour.toString()
            itemHoursDone.text = employee.hourDone.toString()
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){ itemPhoto -> {} }
        }
    }

    override fun getItemCount(): Int = listEmployeeHourExtra.count()

    fun updateHour(list: ArrayList<EntityDashboard>) {
        listEmployeeHourExtra = list
        notifyDataSetChanged()
    }

}