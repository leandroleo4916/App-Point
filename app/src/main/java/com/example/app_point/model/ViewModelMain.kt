package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.PointsEntity
import com.example.app_point.interfaces.RepositoryData
import com.example.app_point.repository.RepositoryPoint

class ViewModelMain (application: Application, private var repository: RepositoryData):
    AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<ArrayList<PointsEntity?>>()
    val employeeFullList: LiveData<ArrayList<PointsEntity?>> = mEmployeeFullList

    fun getFullEmployee(employee: String){
        if (employee.isEmpty()){ mEmployeeFullList.value = repository.fullPoints() }
        else { mEmployeeFullList.value = repository.fullPointsToName(employee, "") }
    }

}