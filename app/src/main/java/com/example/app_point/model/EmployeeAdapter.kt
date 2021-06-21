package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EmployeeDados
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.ConverterPhoto
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.local.QueryResult
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNREACHABLE_CODE")
class EmployeeAdapter(private val application: Application) : RecyclerView.Adapter<EmployeeViewHolder>() {

    private var mListFullEmployee: ArrayList<EmployeeDados> = arrayListOf()
    //private var mListFullPoints: ArrayList<PointsEntity> = arrayListOf()
    private val mPoints: RepositoryPoint = RepositoryPoint(application)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    // Create the list of the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_employee, parent, false)

        // Add animation to item RecyclerView
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return EmployeeViewHolder(item)
    }

    // Send to ViewHolder item of List
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val fullEmployee = mListFullEmployee[position]
        holder.bind(fullEmployee.nameEmployee, fullEmployee.cargoEmployee, fullEmployee.admissaoEmployee)

        val photoConvert = fullEmployee.photo.let { mConverterPhoto.converterStringToBitmap(it) }
        photoConvert.let { holder.bindPhoto(it) }

        val date = Calendar.getInstance().time
        val dateTime = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
        val dateCurrent = dateTime.format(date)
        val fullPoints = mPoints.selectFullPoints(fullEmployee.nameEmployee, dateCurrent)
        fullPoints?.let { holder.bindHora(it, "") }
    }

    override fun getItemCount(): Int {
        return mListFullEmployee.count()
        //return mListFullPoints.count()
    }

    fun updateFullEmployee(list: QuerySnapshot){
        for (element in list){
            mListFullEmployee.add(EmployeeDados(element["photo"] as String, element["horario1"] as String,
                element["horario2"] as String, element["horario3"] as String, element["horario4"] as String,
                element["nameEmployee"] as String, element["emailEmployee"] as String, element["cargoEmployee"] as String,
                element["phoneEmployee"] as String, element["admissaoEmployee"] as String, element["aniversarioEmployee"] as String))

            notifyDataSetChanged()
        }
    }
}