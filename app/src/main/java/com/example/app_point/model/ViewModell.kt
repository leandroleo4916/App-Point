package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.RepositoryData

class ViewModel (application: Application, private var mSearchRecycler: RepositoryData):
    AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<ArrayList<PointsEntity>>()
    val employeeFullList: LiveData<ArrayList<PointsEntity>> = mEmployeeFullList

    fun getFullEmployee(employee: String){
        if (employee == ""){ mEmployeeFullList.value = mSearchRecycler.fullPoints() }
        else { mEmployeeFullList.value = mSearchRecycler.fullPointsToName(employee, "") }
    }

}