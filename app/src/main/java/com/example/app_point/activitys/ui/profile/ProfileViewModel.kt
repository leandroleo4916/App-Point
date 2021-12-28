package com.example.app_point.activitys.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.PointsEntity
import com.example.app_point.entity.PointsFullEntity
import com.example.app_point.interfaces.RepositoryData

class ProfileViewModel(private val searchRecycler: RepositoryData) : ViewModel() {

    private val pointsFullList = MutableLiveData<PointsFullEntity?>()
    val pointsList: LiveData<PointsFullEntity?> = pointsFullList

    fun getFullPoints(employee: String, date: String){
        pointsFullList.value = searchRecycler.fullPointsByNameAndDate(employee, date)
    }

}