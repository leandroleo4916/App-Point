package com.example.app_point.model

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.utils.ConverterPhoto
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.coroutineContext

@Suppress("UNREACHABLE_CODE")
class PontosAdapter(private val application: Application) : RecyclerView.Adapter<PontosViewHolder>() {

    private var mListFuncionario: List<String> = arrayListOf()
    private var mListData: List<String> = arrayListOf()
    private var mListHora: List<String> = arrayListOf()
    private val mPhoto: BusinessEmployee = BusinessEmployee(application)
    private val mConverterPhoto: ConverterPhoto = ConverterPhoto()

    // cria a listagem do layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PontosViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_points, parent, false)

        // Add animation to item RecyclerView
        val animation: Animation = AnimationUtils.loadAnimation( application, R.anim.zoom)
        item.startAnimation(animation)

        return PontosViewHolder(item)
    }

    // Envia para ViewHolder cada item da Lista
    override fun onBindViewHolder(holder: PontosViewHolder, position: Int) {
        holder.bind(mListFuncionario[position])

        // Captura nome do cliente e busca foto no DB
        val photo = mPhoto.consultPhoto(mListFuncionario[position])

        // Converte foto
        val photoConvert = mConverterPhoto.converterToBitmap(photo!!)

        holder.bindPhoto(photoConvert)
        holder.bindData(mListData[position])
        holder.bindHora(mListHora[position])
    }

    override fun getItemCount(): Int {
        return mListFuncionario.count()
        return mListData.count()
        return mListHora.count()
    }

    // Função inverte a lista
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