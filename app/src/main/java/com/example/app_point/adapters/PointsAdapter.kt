package com.example.app_point.adapters

import android.app.Application
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.PointsEntity
import com.example.app_point.utils.ConverterPhoto

class PointsAdapter (private val application: Application, private val mBusiness: BusinessEmployee) :
    RecyclerView.Adapter<PointsAdapter.PointsViewHolder>() {

    private var listFullEmployee: ArrayList<PointsEntity?> = arrayListOf()
    private val converterPhoto: ConverterPhoto = ConverterPhoto()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)

        val animation: Animation = AnimationUtils.loadAnimation(application, R.anim.animation_left)
        item.startAnimation(animation)

        return PointsViewHolder(item)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {

        val fullEmployee = listFullEmployee[position]

        val photo = fullEmployee?.idEmployee?.let { mBusiness.consultPhoto(it) }
        val name = fullEmployee?.idEmployee?.let { mBusiness.consultNameEmployeeById(it) }
        val photoConvert = photo?.let { converterPhoto.converterToBitmap(it) }
        photoConvert?.let { holder.bindPhoto(it) }

        name?.let { holder.bind(it) }
        holder.bindData(fullEmployee?.data.toString())
        holder.bindHora(fullEmployee, position)
    }

    inner class PointsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(employee: String){
            itemView.findViewById<TextView>(R.id.text_view_name_employee).text = employee
        }

        fun bindData (data: String){ itemView.findViewById<TextView>(R.id.text_data).text = data }

        fun bindHora(fullEmployee: PointsEntity?, position: Int){

            val textHora1 = itemView.findViewById<TextView>(R.id.text_hora)
            val textHora2 = itemView.findViewById<TextView>(R.id.text_hora2)
            val textHora3 = itemView.findViewById<TextView>(R.id.text_hora3)
            val textHora4 = itemView.findViewById<TextView>(R.id.text_hora4)
            val constraint: ConstraintLayout = itemView.findViewById(R.id.toolbar_employee)
            val toolbarLine: Toolbar = itemView.findViewById(R.id.toolbar_line_recycler_detail)

            textHora1.text = fullEmployee?.hora1 ?: "--:--"
            textHora2.text = fullEmployee?.hora2 ?: "--:--"
            textHora3.text = fullEmployee?.hora3 ?: "--:--"
            textHora4.text = fullEmployee?.hora4 ?: "--:--"

            if ((listFullEmployee.size - 1) == position){
                constraint.setBackgroundResource(R.drawable.back_radius_superior_retas)
                toolbarLine.visibility = View.GONE
            }
        }

        fun bindPhoto(image: Bitmap){
            itemView.findViewById<ImageView>(R.id.icon_image_perfil).setImageBitmap(image)
        }

    }

    override fun getItemCount(): Int {
        return listFullEmployee.count()
    }

    fun updateFullPoints(list: ArrayList<PointsEntity?>) {
        val listReverse = list.reversed()
        listFullEmployee = when {
            listReverse.size > 1 -> listReverse as ArrayList<PointsEntity?>
            else -> list
        }
        notifyDataSetChanged()
    }
}