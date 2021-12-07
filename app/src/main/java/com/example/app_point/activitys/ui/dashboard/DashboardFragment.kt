package com.example.app_point.activitys.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.repository.RepositoryPoint
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.image_toolbar_hrs
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private lateinit var binding: View
    private val viewModelDash: DashboardViewModel by viewModel()
    private val businessEmployee: BusinessEmployee by inject()
    private val repositoryPoint: RepositoryPoint by inject()
    private val adapterRanking: AdapterDashboardRanking by inject()
    private val adapterDetail: AdapterDashboardDetail by inject()

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_detail, container, false)

        recycler()
        viewModelDash()
        observe()

        return binding
    }

    private fun viewModelDash(){
        viewModelDash.consultEmployeeAndPoints()
    }

    private fun recycler() {
        val recyclerRanking = binding.recycler_ranking
        recyclerRanking.layoutManager = LinearLayoutManager(context)
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
                            binding.image_toolbar_hrs.progress = value
                            binding.text_total_funcionario.text = it.toString()
                        }
                        delay(20)
                        value++
                    }
                }
            }
        })
    }
}