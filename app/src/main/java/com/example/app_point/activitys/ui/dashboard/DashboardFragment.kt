package com.example.app_point.activitys.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private lateinit var binding: View
    private val viewModelDash: DashboardViewModel by viewModel()
    private val adapterRanking: AdapterDashboardRanking by inject()
    private val adapterDetail: AdapterDashboardDetail by inject()

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_detail, container, false)

        recycler()
        viewModelDash()
        observe()
        listener()

        return binding
    }

    private fun listener(){
        binding.image_back_perfil.setOnClickListener { activity?.onBackPressed() }
    }

    private fun viewModelDash(){
        viewModelDash.consultEmployeeAndPoints()
    }

    private fun recycler() {
        val recyclerRanking = binding.recycler_ranking
        recyclerRanking.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerRanking.adapter = adapterRanking

        val recyclerDetail = binding.recycler_detail
        recyclerDetail.layoutManager = LinearLayoutManager(context)
        recyclerDetail.adapter = adapterDetail
    }

    private fun observe(){

        viewModelDash.employeeTotal.observe (viewLifecycleOwner, {

            var value = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (value <= it) {
                        withContext(Dispatchers.Main) {
                            binding.progress_total_funcionarios.progress = value
                            binding.text_total_funcionario.text = value.toString()
                        }
                        delay(100)
                        value++
                    }
                }
            }
        })

        viewModelDash.employeeVacation.observe (viewLifecycleOwner, {

            var value = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (value <= it) {
                        withContext(Dispatchers.Main) {
                            binding.progress_funcionarios_ferias.progress = value
                            binding.text_funcionarios_ferias.text = value.toString()
                        }
                        delay(100)
                        value++
                    }
                }
            }
        })

        viewModelDash.employeeActive.observe (viewLifecycleOwner, {

            var value = 0
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    while (value <= it) {
                        withContext(Dispatchers.Main) {
                            binding.progress_funcionarios_ativos.progress = value
                            binding.text_funcionarios_ativos.text = value.toString()
                        }
                        delay(100)
                        value++
                    }
                }
            }
        })

        viewModelDash.employeeBest.observe(viewLifecycleOwner, {
            when(it.size){
                0 -> {
                    binding.progress_ranking.visibility = View.GONE
                    binding.text_nenhum_funcionario.visibility = View.VISIBLE
                }
                else -> {
                    adapterRanking.updateRanking(it)
                    binding.progress_ranking.visibility = View.GONE
                    binding.text_nenhum_funcionario.visibility = View.INVISIBLE
                }
            }
        })

        viewModelDash.employeeDetail.observe(viewLifecycleOwner, {
            when(it.size){
                0 -> {
                    binding.progress_detail.visibility = View.GONE
                    binding.text_nenhum_funcionario_recycler.visibility = View.VISIBLE
                }
                else -> {
                    adapterDetail.updateDetail(it)
                    binding.progress_detail.visibility = View.GONE
                    binding.text_nenhum_funcionario_recycler.visibility = View.INVISIBLE
                }
            }
        })
    }
}