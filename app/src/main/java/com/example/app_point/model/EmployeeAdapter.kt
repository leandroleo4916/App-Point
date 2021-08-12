package com.example.app_point.model

import android.app.Application
import android.graphics.Bitmap
import android.icu.util.Calendar
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EmployeeAdapter(private val listener: OnItemClickRecycler, private var searchPoints: RepositoryPoint):
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var mListFullEmployee: ArrayList<EmployeeEntity> = arrayListOf()
    private var dateCurrent: String = ""
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val fullEmployee = mListFullEmployee[position]
        val photoConvert = fullEmployee.photo.let { mConverterPhoto.converterToBitmap(it) }

        holder.bind(fullEmployee.nameEmployee, fullEmployee.cargoEmployee, fullEmployee.admissaoEmployee)
        holder.bindPhoto(photoConvert)

        val points = searchPoints.fullPointsToName(fullEmployee.nameEmployee, dateCurrent)
        holder.bindData(dateCurrent)
        holder.bindHora(points)
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        //private val mAddHours: ConverterHours = ConverterHours()

        fun bind(employee: String, cargo: String, admission: String){

            val textEmployee = itemView.findViewById<TextView>(R.id.text_nome_employee)
            textEmployee.text = employee
            val textCargo = itemView.findViewById<TextView>(R.id.text_cargo)
            textCargo.text = cargo
            val textAdmission = itemView.findViewById<TextView>(R.id.text_data_admission)
            textAdmission.text = admission
        }

        fun bindData(date: String){

            val calendar = Calendar.getInstance().time
            val dateCalendar = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
            val vDate = dateCalendar.format(calendar)
            val textData = itemView.findViewById<TextView>(R.id.text_data)

            when (date) {
                vDate -> { }
                else -> { textData.text = date }
            }
        }

        fun bindHora(points: ArrayList<PointsEntity>){

            val textHora1 = itemView.findViewById<TextView>(R.id.text_hora1)
            val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
            val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
            val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)

            //val minutesCurrent = mAddHours.converterHoursInMinutes(points[0].hora1!!)
            //val minutes = mAddHours.converterHoursInMinutes(hora1)

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
            /*
            when {
                minutesCurrent < minutes -> {
                    imageBack.setImageResource(R.color.colorGreen)
                }
                minutesCurrent > minutes + 15 -> {
                    imageBack.setImageResource(R.color.colorRed)
                }
                else -> {
                    imageBack.setImageResource(R.color.colorYellow)
                }
            }
             */
        }

        fun bindPhoto(image: Bitmap){
            val imageView = itemView.findViewById<ImageView>(R.id.icon_image_perfil)
            imageView.setImageBitmap(image)
        }

        init {
            itemView.edit_employee.setOnClickListener(this)
            itemView.remove_employee.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){
                itemView.remove_employee -> listener.clickRemove(position)
                itemView.edit_employee -> listener.clickEdit(position)
                itemView.back -> {}
                itemView.next -> {}
            }
        }
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
    }

    fun updateDateCurrent(date: String){
        dateCurrent = date
    }

    fun updateFullEmployee(list: ArrayList<EmployeeEntity>){
        mListFullEmployee = list
        notifyDataSetChanged()
    }
}