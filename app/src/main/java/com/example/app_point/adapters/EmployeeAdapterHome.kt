package com.example.app_point.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EmployeeNameAndPhoto
import com.example.app_point.interfaces.ItemClickOpenProfileById
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.utils.GetColor

class EmployeeAdapterHome (private val listener: ItemClickOpenProfileById,
                           private val color: GetColor,
                           private val converterPhoto: ConverterPhoto):
    RecyclerView.Adapter<EmployeeAdapterHome.EmployeeViewHolderHome>() {

    private var listFullEmployee: ArrayList<EmployeeNameAndPhoto> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolderHome {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        return EmployeeViewHolderHome(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolderHome, position: Int) {

        val fullEmployee = listFullEmployee[position]
        val photoConvert = converterPhoto.converterToBitmap(fullEmployee.photo)
        holder.bindNameAndPhoto(fullEmployee.name, photoConvert, position)
    }

    inner class EmployeeViewHolderHome(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemName: TextView = itemView.findViewById(R.id.text_employee_home)
        private val itemPhoto: ImageView = itemView.findViewById(R.id.image_employee_home)
        private val itemBox: ConstraintLayout = itemView.findViewById(R.id.box_employee)

        init {
            itemName.setOnClickListener(this)
            itemPhoto.setOnClickListener(this)
        }

        fun bindNameAndPhoto(employee: String, photo: Bitmap, position: Int){
            itemName.text = employee
            itemPhoto.setImageBitmap(photo)
            itemBox.setBackgroundResource(color.retColor(position))
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){ itemPhoto -> listener.openFragmentProfileById(listFullEmployee[position]) }
        }
    }

    override fun getItemCount(): Int {
        return listFullEmployee.count()
    }

    fun updateEmployee(list: ArrayList<EmployeeNameAndPhoto>) {
        listFullEmployee = list
        notifyDataSetChanged()
    }

}