package com.example.app_point.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_point.R
import com.example.app_point.model.PointsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mPoints: PointsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val float = root.findViewById(R.id.float_button_point) as FloatingActionButton
        float.setOnClickListener { registerPoints() }

        mPoints = PointsAdapter()

        val recycler = root.findViewById<RecyclerView>(R.id.recyclerEmployee)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mPoints

        return root
    }

    private fun registerPoints(){

    }

}