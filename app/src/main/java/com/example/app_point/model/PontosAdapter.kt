package com.example.app_point.activitys

import android.app.Application
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntity
import java.io.ByteArrayInputStream

class PontosAdapter(application: Application) : RecyclerView.Adapter<PontosViewHolder>() {

    private var mListFuncionario: List<String> = arrayListOf()
    private var mListData: List<String> = arrayListOf()
    private var mListHora: List<String> = arrayListOf()
    private val mPhoto: BusinessEmployee = BusinessEmployee(application)

    // cria a listagem do layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontosViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)
        return PontosViewHolder(item)
    }

    override fun onBindViewHolder(holder: PontosViewHolder, position: Int) {
        holder.bind(mListFuncionario[position])
        val photo = mPhoto.consultPhoto(mListFuncionario[position])
        val photoBitmap = ByteArrayInputStream(photo)
        val photoConvert = BitmapFactory.decodeStream(photoBitmap)
        holder.bindPhoto(photoConvert)
        holder.bindData(mListData[position])
        holder.bindHora(mListHora[position])
    }

    override fun getItemCount(): Int {
        return mListFuncionario.count()
        return mListData.count()
        return mListHora.count()
    }

    fun updateFuncionario(list: List<String>){
        mListFuncionario = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateData(list: List<String>){
        mListData = list.asReversed()
        notifyDataSetChanged()
    }
    fun updateHora(list: List<String>){
        mListHora = list.asReversed()
        notifyDataSetChanged()
    }
}