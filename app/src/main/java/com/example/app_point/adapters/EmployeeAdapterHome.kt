package com.example.app_point.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemClickEmployeeHome
import com.example.app_point.utils.ConverterPhoto

class EmployeeAdapterHome(private val application: Context?,
                          private val listener: ItemClickEmployeeHome) :
    RecyclerView.Adapter<EmployeeAdapterHome.EmployeeViewHolderHome>() {

    private var listFullEmployee: ArrayList<EmployeeEntity> = arrayListOf()
    private val converterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolderHome {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation(application, R.anim.animation_left)
        item.startAnimation(animation)

        return EmployeeViewHolderHome(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolderHome, position: Int) {

        val fullEmployee = listFullEmployee[position]
        val photoConvert = converterPhoto.converterToBitmap(fullEmployee.photo)

        holder.bindNameAndPhoto(fullEmployee.nameEmployee, photoConvert)
    }

    inner class EmployeeViewHolderHome(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemName: TextView = itemView.findViewById(R.id.text_employee_home)
        private val itemPhoto: ImageView = itemView.findViewById(R.id.image_employee_home)

        init {
            itemName.setOnClickListener(this)
            itemPhoto.setOnClickListener(this)
        }

        fun bindNameAndPhoto(employee: String, photo: Bitmap){
            itemName.text = employee
            itemPhoto.setImageBitmap(photo)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){ itemPhoto -> listener.openFragmentProfile(listFullEmployee[position]) }
        }
    }

    override fun getItemCount(): Int {
        return listFullEmployee.count()
    }

    fun updateEmployee(list: ArrayList<EmployeeEntity>) {
        listFullEmployee = list
        notifyDataSetChanged()
    }

}