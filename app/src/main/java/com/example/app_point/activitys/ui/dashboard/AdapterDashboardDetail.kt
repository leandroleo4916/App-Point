package com.example.app_point.activitys.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EntityDashboard
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.fragment_detail.view.*

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
        holder.bindPhotoAndHour(fullEmployee, position)

    }

    inner class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemPhoto: ImageView = itemView.findViewById(R.id.image_perfil_detail_dash)
        private val itemHoursExtras: TextView = itemView.findViewById(R.id.text_hours_extras_detail)
        private val itemHoursDone: TextView = itemView.findViewById(R.id.text_hours_feitas_detail)
        private val toolbarLine: Toolbar = itemView.findViewById(R.id.toolbar_line_recycler_detail)
        private val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint_employee_detail)

        init { itemPhoto.setOnClickListener(this) }

        fun bindPhotoAndHour(employee: EntityDashboard, position: Int){
            val photo = converterPhoto.converterToBitmap(employee.photo)
            itemPhoto.setImageBitmap(photo)
            itemHoursExtras.text = employee.extraHour
            itemHoursDone.text = employee.hourDone

            if((listEmployeeHourExtra.size - 1) == position){
                toolbarLine.visibility = View.INVISIBLE
                constraint.setBackgroundResource(R.drawable.back_radius_superior_retas)
            }
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){ itemPhoto -> {} }
        }
    }

    override fun getItemCount(): Int = listEmployeeHourExtra.count()

    fun updateDetail(list: ArrayList<EntityDashboard>) {
        listEmployeeHourExtra = list
        notifyDataSetChanged()
    }

}