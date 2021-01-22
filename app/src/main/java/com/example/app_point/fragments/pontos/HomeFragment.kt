package com.example.app_point.fragments.pontos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.PointsAdapter
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mRepositoryEmployee: RepositoryEmployee
    private lateinit var mRepositoryPoint: RepositoryPoint
    private var mPoints: PointsAdapter = PointsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val float = root.findViewById(R.id.float_button) as FloatingActionButton
        float.setOnClickListener {  }

        val recycler = root.findViewById<RecyclerView>(R.id.recyclerPonto)
        recycler.layoutManager = LinearLayoutManager(context)
        //recycler.adapter = mPoints

        mRepositoryEmployee = RepositoryEmployee(context)
        mRepositoryPoint = RepositoryPoint(context)

        homeViewModel.getEmployeeList()
        homeViewModel.getHourList()
        homeViewModel.getDateList()

        return root
    }

}