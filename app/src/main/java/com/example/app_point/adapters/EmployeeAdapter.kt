package com.example.app_point.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.Employee
import com.example.app_point.entity.PointsHours
import com.example.app_point.interfaces.INotification
import com.example.app_point.interfaces.ItemEmployee
import com.example.app_point.interfaces.OnItemClickRecycler
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.activity_perfil.view.edit_employee
import kotlinx.android.synthetic.main.recycler_employee_detail.view.*
import java.util.*
import kotlin.collections.ArrayList

class EmployeeAdapter(private var searchPoints: RepositoryPoint,
                      private val listener: OnItemClickRecycler,
                      private val listenerFragment: ItemEmployee,
                      private val notification: INotification,
                      private val application: Context?):
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>(), Filterable {

    private var data = mutableListOf<Employee>()
    private var listEmployee = mutableListOf<Employee>()
    private val converterPhoto: ConverterPhoto = ConverterPhoto()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_employee_detail, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.animation_left)
        item.startAnimation(animation)

        return EmployeeViewHolder(item)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val employee = listEmployee[position]
        val photoConvert = employee.photo.let { converterPhoto.converterToBitmap(it) }

        holder.run {
            bind(employee.name, employee.cargo, employee.admission)
            bindPhoto(photoConvert)
            bindHora(employee)
            bindButtom(employee.date)
        }
    }

    override fun getItemCount(): Int { return listEmployee.size }

    override fun getFilter(): Filter {
        return filter
    }

    inner class EmployeeViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.edit_employee.setOnClickListener(this)
            itemView.remove_employee.setOnClickListener(this)
            itemView.next.setOnClickListener(this)
            itemView.back.setOnClickListener(this)
            itemView.toolbar1.setOnClickListener(this)
            itemView.toolbar2.setOnClickListener(this)
            itemView.toolbar3.setOnClickListener(this)
            itemView.toolbar4.setOnClickListener(this)
            itemView.icon_image_perfil.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            val id = listEmployee[position].id
            val name = listEmployee[position].name
            val date = listEmployee[position].date
            val hour1 = listEmployee[position].hour1
            val hour2 = listEmployee[position].hour2
            val hour3 = listEmployee[position].hour3
            val hour4 = listEmployee[position].hour4
            val image = listEmployee[position].photo

            when(view){
                itemView.remove_employee -> listener.clickRemove(id, name, position)
                itemView.edit_employee -> listenerFragment.openFragmentRegister(id)
                itemView.back -> listener.clickBack(name, date, position)
                itemView.next -> listener.clickNext(name, date, position)
                itemView.toolbar1 -> listener.clickHour(name, date, 1, hour1, position)
                itemView.toolbar2 -> listener.clickHour(name, date, 2, hour2, position)
                itemView.toolbar3 -> listener.clickHour(name, date, 3, hour3, position)
                itemView.toolbar4 -> listener.clickHour(name, date, 4, hour4, position)
                itemView.icon_image_perfil -> listener.clickImage(image, name)
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

        fun bindButtom(date: String){

            if (date == "Hoje"){
                itemView.next.run {
                    isEnabled = false
                    setImageResource(R.drawable.ic_next_gray)
                }
            }
            else {
                itemView.next.run {
                    isEnabled = true
                    setImageResource(R.drawable.ic_next_write)
                }
            }
        }
    }

    fun updateFullEmployee(list: ArrayList<Employee>, date: String){

        for (position in 0 until list.size){
            val points = searchPoints.selectFullPoints(list[position].name, date)
            list[position].date = "Hoje"
            list[position].hour1 = points?.hora1 ?: "--:--"
            list[position].hour2 = points?.hora2 ?: "--:--"
            list[position].hour3 = points?.hora3 ?: "--:--"
            list[position].hour4 = points?.hora4 ?: "--:--"
        }
        data.addAll(list)
        listEmployee.addAll(list)
        getFilter()
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

    fun updateHour(positionHour: Int, hour: String, position: Int){

        when(positionHour) {
            1 -> listEmployee[position].hour1 = hour
            2 -> listEmployee[position].hour2 = hour
            3 -> listEmployee[position].hour3 = hour
            else -> listEmployee[position].hour4 = hour
        }
        notifyDataSetChanged()
    }

    fun swapPosition(source: Int, destination: Int) {
        Collections.swap(listEmployee, source, destination)
        notifyItemMoved(source, destination)
    }

    fun removeEmployee(position: Int){
        listener.clickRemove(listEmployee[position].id, listEmployee[position].name, position)
    }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {

            val filterResults = FilterResults()
            if (constraint != null) {
                val list = ArrayList<Employee>()

                for (employee in data) {
                    if (employee.name.toLowerCase(Locale.ROOT)
                            .contains(constraint.toString().toLowerCase(Locale.ROOT))
                    ) {
                        list.add(employee)
                    }
                }
                filterResults.count = list.size
                filterResults.values = list

            } else {
                filterResults.count = data.size
                filterResults.values = data
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            if (results.values is ArrayList<*>) {

                listEmployee = results.values as MutableList<Employee>
                when {listEmployee.isEmpty() -> notification.notification() }
            }
            notifyDataSetChanged()
        }
    }
}