package com.example.app_point.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.Employee
import com.example.app_point.entity.PointsHours
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.recycler_employee.view.*
import java.util.*
import kotlin.collections.ArrayList

class EmployeeAdapter(private var searchPoints: RepositoryPoint, private val listener: OnItemClickRecycler):
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var listEmployee: ArrayList<Employee> = arrayListOf()
    private val converterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val employee = listEmployee[position]
        val photoConvert = employee.photo.let { converterPhoto.converterToBitmap(it) }

        holder.run {
            bind(employee.name, employee.cargo, employee.admission)
            bindPhoto(photoConvert)
            bindHora(employee)
        }
    }

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.edit_employee.setOnClickListener(this)
            itemView.remove_employee.setOnClickListener(this)
            itemView.next.setOnClickListener(this)
            itemView.back.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            val id = listEmployee[position].id
            val name = listEmployee[position].name
            val date = listEmployee[position].date

            when(view){
                itemView.remove_employee -> listener.clickRemove(id, name)
                itemView.edit_employee -> listener.clickEdit(id)
                itemView.back -> listener.clickBack(name, date, position)
                itemView.next -> listener.clickNext(name, date, position)
            }
        }

        fun bind(employee: String, cargo: String, admission: String){
            itemView.run {
                findViewById<TextView>(R.id.text_view_name_employee).text = employee
                findViewById<TextView>(R.id.text_cargo).text = cargo
                findViewById<TextView>(R.id.text_data_admission).text = admission
            }
        }

        fun bindHora(employee: Employee){

            val textHora1 = itemView.findViewById<TextView>(R.id.text_hora1)
            val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
            val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
            val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)
            val textDate = itemView.findViewById<TextView>(R.id.text_data_ponto)

            textHora1.text = employee.hour1
            textHora2.text = employee.hour2
            textHora3.text = employee.hour3
            textHora4.text = employee.hour4
            textDate.text = employee.date
        }

        fun bindPhoto(image: Bitmap){
            itemView.findViewById<ImageView>(R.id.icon_image_perfil).setImageBitmap(image)
        }
    }

    override fun getItemCount(): Int { return listEmployee.count() }

    fun updateFullEmployee(list: ArrayList<Employee>, date: String){

        for (position in 0 until list.size){
            val points = searchPoints.selectFullPoints(list[position].name, date)
            list[position].date = "Hoje"
            list[position].hour1 = points?.hora1 ?: "--:--"
            list[position].hour2 = points?.hora2 ?: "--:--"
            list[position].hour3 = points?.hora3 ?: "--:--"
            list[position].hour4 = points?.hora4 ?: "--:--"
        }
        listEmployee = list
        notifyDataSetChanged()
    }

    fun updateDate(list: PointsHours?, dateCurrent: String, dateFinal: String, position: Int){

        when {
            dateFinal != dateCurrent -> { listEmployee[position].date = dateFinal }
            else -> { listEmployee[position].date = "Hoje" }
        }

        listEmployee[position].hour1 = list?.horario1 ?: "--:--"
        listEmployee[position].hour2 = list?.horario2 ?: "--:--"
        listEmployee[position].hour3 = list?.horario3 ?: "--:--"
        listEmployee[position].hour4 = list?.horario4 ?: "--:--"
        notifyDataSetChanged()
    }

    fun swap(source: Int, destination: Int) {
        Collections.swap(listEmployee, source, destination)
        notifyItemMoved(source, destination)
    }
}