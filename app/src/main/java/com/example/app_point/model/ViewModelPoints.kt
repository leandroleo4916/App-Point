package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.RepositoryData
import com.example.app_point.repository.RepositoryPoint

class ViewModelPoints (application: Application, private val searchRecycler: RepositoryData):
    AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<ArrayList<PointsEntity>>()
    val employeeFullList: LiveData<ArrayList<PointsEntity>> = mEmployeeFullList

    fun getFullEmployee(employee: String, date: String){
        if (employee == ""){ mEmployeeFullList.value = searchRecycler.fullPoints() }
        else { mEmployeeFullList.value = searchRecycler.fullPointsToName(employee, date) }
    }

    fun getFullPointsByName(employee: String): ArrayList<HoursEntity>{
        return searchRecycler.fullPointsByName(employee)
    }
}