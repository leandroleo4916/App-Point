package com.example.app_point.activitys.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.entity.EntityDashboard
import com.example.app_point.utils.ConverterPhoto
import kotlinx.coroutines.*

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

        private val itemHoursExtras: TextView = itemView.findViewById(R.id.text_hours_extra_detail)
        private val progressHoursExtra: ProgressBar = itemView.findViewById(R.id.progress_hours_extras_cyrcle)

        private val itemHoursDone: TextView = itemView.findViewById(R.id.text_hours_done_detail)
        private val progressHoursDone: ProgressBar = itemView.findViewById(R.id.progress_hours_done_cyrcle)

        private val itemCadastro: TextView = itemView.findViewById(R.id.text_hours_cadastro_detail)
        private val progressCadastro: ProgressBar = itemView.findViewById(R.id.progress_cadastro_cyrcle)

        private val itemPhoto: ImageView = itemView.findViewById(R.id.image_perfil_detail_dash)
        private val constraint: ConstraintLayout = itemView.findViewById(R.id.constraint_employee_detail)

        init { itemPhoto.setOnClickListener(this) }

        fun bindPhotoAndHour(employee: EntityDashboard, position: Int){
            val photo = converterPhoto.converterToBitmap(employee.photo)
            itemPhoto.setImageBitmap(photo)

            val extraCurrent = employee.extraHour / 60
            var extra = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (extra <= extraCurrent) {
                        withContext(Dispatchers.Main) {
                            progressHoursExtra.progress = extra
                            if (extra <= 1){ itemHoursExtras.text = "$extra"+"h" }
                            else{ itemHoursExtras.text = "$extra"+"hs" }

                        }
                        delay(30)
                        extra++
                    }
                }
            }

            val doneCurrent = employee.hourDone / 60
            var done = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (done <= doneCurrent) {
                        withContext(Dispatchers.Main) {
                            progressHoursDone.progress = done
                            if (done <= 1){ itemHoursDone.text = "$done"+"h" }
                            else{ itemHoursDone.text = "$done"+"hs" }
                        }
                        delay(30)
                        done++
                    }
                }
            }

            var cadastro = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (cadastro <= employee.cadastro) {
                        withContext(Dispatchers.Main) {
                            progressCadastro.progress = cadastro
                            itemCadastro.text = "$cadastro"+"%"
                        }
                        delay(30)
                        cadastro++
                    }
                }
            }

            if((listEmployeeHourExtra.size - 1) == position){
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