package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.PointsEntity
import com.example.app_point.repository.RepositoryPoint

class ViewModel (application: Application): AndroidViewModel(application) {

    private var mSearchRecycler: RepositoryPoint = RepositoryPoint (application)

    private val mEmployeeFullList = MutableLiveData<ArrayList<PointsEntity>>()
    val employeeFullList: LiveData<ArrayList<PointsEntity>> = mEmployeeFullList

    fun getFullEmployee(employee: String){
        if (employee == ""){
            mEmployeeFullList.value = mSearchRecycler.fullPoints()
        }else {
            mEmployeeFullList.value = mSearchRecycler.fullPointsToName(employee, "")
        }
    }

}