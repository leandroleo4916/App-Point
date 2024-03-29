package com.example.app_point.activitys.ui.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.RepositoryData

class PointsViewModel (private var repository: RepositoryData) : ViewModel() {

    private val mEmployeeFullList = MutableLiveData<ArrayList<PointsEntity?>>()
    val employeeFullList: LiveData<ArrayList<PointsEntity?>> = mEmployeeFullList

    fun getFullPoints(id: Int){
        if (id == 0){ mEmployeeFullList.value = repository.fullPoints() }
        else { mEmployeeFullList.value = repository.fullPointsToName(id, "") }
    }
}