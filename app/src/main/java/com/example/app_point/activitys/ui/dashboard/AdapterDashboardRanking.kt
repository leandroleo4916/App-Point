package com.example.app_point.activitys.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EntityDashboard
import com.example.app_point.utils.ConverterPhoto

class AdapterDashboardRanking (private val converterPhoto: ConverterPhoto):
    RecyclerView.Adapter<AdapterDashboardRanking.ViewHolderRanking>() {

    private var listEmployeeHourExtra: ArrayList<EntityDashboard> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRanking {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_ranking, parent, false)

        return ViewHolderRanking(item)
    }

    override fun onBindViewHolder(holder: ViewHolderRanking, position: Int) {
        val fullEmployee = listEmployeeHourExtra[position]
        holder.bindPhotoAndHour(fullEmployee, position)
    }

    inner class ViewHolderRanking(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val itemPhoto: ImageView = itemView.findViewById(R.id.icon_image_ranking)
        private val itemMedal: ImageView = itemView.findViewById(R.id.image_medal)

        init { itemPhoto.setOnClickListener(this) }

        fun bindPhotoAndHour(employee: EntityDashboard, position: Int){

            val photo = converterPhoto.converterToBitmap(employee.photo)
            itemPhoto.setImageBitmap(photo)
            when (position){
                0 -> itemMedal.setImageResource(R.drawable.ic_medal_ouro)
                1 -> itemMedal.setImageResource(R.drawable.ic_medal_prata)
                2 -> itemMedal.setImageResource(R.drawable.ic_medal_bronze)
                else -> itemMedal.visibility = View.INVISIBLE
            }
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            when(view){ itemPhoto -> {} }
        }
    }

    override fun getItemCount(): Int = listEmployeeHourExtra.count()

    fun updateRanking(list: ArrayList<EntityDashboard>) {
        listEmployeeHourExtra = list
        notifyDataSetChanged()
    }

}