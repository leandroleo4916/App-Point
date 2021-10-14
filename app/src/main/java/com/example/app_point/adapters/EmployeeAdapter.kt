package com.example.app_point.adapters

import android.app.Application
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.recycler_employee.view.*
import kotlin.collections.ArrayList

class EmployeeAdapter(application: Application, private val listener: OnItemClickRecycler):
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var mListFullEmployee: ArrayList<EmployeeEntity> = arrayListOf()
    private var dateCurrent: String = ""
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()
    private var searchPoints: RepositoryPoint = RepositoryPoint (application)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val employee = mListFullEmployee[position]
        val photoConvert = employee.photo.let { mConverterPhoto.converterToBitmap(it) }
        val points = searchPoints.fullPointsToName(employee.nameEmployee, dateCurrent)

        holder.bind(employee.nameEmployee, employee.cargoEmployee, employee.admissaoEmployee)
        holder.bindPhoto(photoConvert)
        holder.bindHora(points)
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(employee: String, cargo: String, admission: String){

            val textEmployee = itemView.findViewById<TextView>(R.id.text_view_name_employee)
            textEmployee.text = employee
            val textCargo = itemView.findViewById<TextView>(R.id.text_cargo)
            textCargo.text = cargo
            val textAdmission = itemView.findViewById<TextView>(R.id.text_data_admission)
            textAdmission.text = admission
        }

        fun bindHora(points: ArrayList<PointsEntity>){

            val textHora1 = itemView.findViewById<TextView>(R.id.text_hora1)
            val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
            val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
            val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)

            when {
                points.size == 0 -> { }
                points[0].hora2 == null -> {
                    textHora1.text = points[0].hora1
                }
                points[0].hora3 == null -> {
                    textHora1.text = points[0].hora1
                    textHora2.text = points[0].hora2
                }
                points[0].hora4 == null -> {
                    textHora1.text = points[0].hora1
                    textHora2.text = points[0].hora2
                    textHora3.text = points[0].hora3
                }
                else -> {
                    textHora1.text = points[0].hora1
                    textHora2.text = points[0].hora2
                    textHora3.text = points[0].hora3
                    textHora4.text = points[0].hora4
                }
            }
        }

        fun bindPhoto(image: Bitmap){
            val imageView = itemView.findViewById<ImageView>(R.id.icon_image_perfil)
            imageView.setImageBitmap(image)
        }

        init {
            itemView.edit_employee.setOnClickListener(this)
            itemView.remove_employee.setOnClickListener(this)
            itemView.next.setOnClickListener(this)
            itemView.back.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            val id = mListFullEmployee[position].id
            val name = mListFullEmployee[position].nameEmployee

            when(view){
                itemView.remove_employee -> listener.clickRemove(id, name)
                itemView.edit_employee -> listener.clickEdit(id)
                itemView.back -> listener.clickBack(name, position)
                itemView.next -> listener.clickNext(name, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
    }

    fun updateFullEmployee(list: ArrayList<EmployeeEntity>, date: String){
        mListFullEmployee = list
        dateCurrent = date
        notifyDataSetChanged()
    }
}