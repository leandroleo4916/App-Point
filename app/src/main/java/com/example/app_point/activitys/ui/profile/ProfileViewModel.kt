package com.example.app_point.activitys.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.RepositoryData

class ProfileViewModel(private val searchRecycler: RepositoryData) : ViewModel() {

    private val pointsFullList = MutableLiveData<ArrayList<PointsEntity?>>()
    val pointsList: LiveData<ArrayList<PointsEntity?>> = pointsFullList

    fun getFullPoints(employee: String, date: String){
        if (employee == ""){ pointsFullList.value = searchRecycler.fullPoints() }
        else { pointsFullList.value = searchRecycler.fullPointsToName(employee, date) }
    }

}