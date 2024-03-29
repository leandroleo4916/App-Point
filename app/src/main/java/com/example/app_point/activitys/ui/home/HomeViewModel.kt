package com.example.app_point.activitys.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.utils.CalculateHours
import com.example.app_point.entity.EmployeeNameAndPhoto
import com.example.app_point.entity.PointsEntity
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class HomeViewModel(private var repository: RepositoryPoint,
                    private var repositoryEmployee: RepositoryEmployee,
                    private val converterHours: CalculateHours) : ViewModel() {

    private val pointsFullList = MutableLiveData<ArrayList<PointsEntity?>>()
    val pointsList: LiveData<ArrayList<PointsEntity?>> = pointsFullList

    private val employeeFullList = MutableLiveData<ArrayList<EmployeeNameAndPhoto>>()
    val employeeList: LiveData<ArrayList<EmployeeNameAndPhoto>> = employeeFullList

    fun getFullPoints(id: Int){
        if (id == 0){ pointsFullList.value = repository.fullPoints() }
        else { pointsFullList.value = repository.fullPointsToName(id, "") }
    }

    fun setPoints(id: Int, name: String, date: String, hour: String): Boolean{
        val hourInt = converterHours.converterHoursInMinutes(hour)
        return repository.setPoint(id, name, date, hour, hourInt)
    }

    fun getFullEmployee(){
        employeeFullList.value = repositoryEmployee.consultEmployeeNameAndPhoto()
    }

}