package com.example.app_point.activitys.ui.points

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_point.R

class PointsFragment : Fragment() {

    private lateinit var pointsViewModel: PointsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        pointsViewModel = ViewModelProvider(this)[PointsViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_points, container, false)

        return root
    }
}