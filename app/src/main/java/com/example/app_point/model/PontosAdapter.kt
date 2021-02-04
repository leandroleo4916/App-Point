package com.example.app_point.activitys

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R

class PontosAdapter : RecyclerView.Adapter<PontosViewHolder>() {

    private var mListFuncionario: List<String> = arrayListOf()
    private var mListData: List<String> = arrayListOf()
    private var mListHora: List<String> = arrayListOf()
    private var mListImage: Bitmap? = null

    // cria a listagem do layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontosViewHolder {

        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)
        return PontosViewHolder(item)
    }

    override fun onBindViewHolder(holder: PontosViewHolder, position: Int) {
        holder.bind(mListFuncionario[position])
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
    fun updateImage(list: Bitmap){
        mListImage = list
        notifyDataSetChanged()
    }
}