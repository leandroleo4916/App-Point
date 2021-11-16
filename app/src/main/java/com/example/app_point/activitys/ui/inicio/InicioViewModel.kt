package com.example.app_point.activitys.ui.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class InicioViewModel(private var repository: RepositoryPoint,
                      private var repositoryEmployee: RepositoryEmployee) : ViewModel() {

    private val pointsFullList = MutableLiveData<ArrayList<PointsEntity?>>()
    val pointsList: LiveData<ArrayList<PointsEntity?>> = pointsFullList

    private val employeeFullList = MutableLiveData<ArrayList<EmployeeEntity>>()
    val employeeList: LiveData<ArrayList<EmployeeEntity>> = employeeFullList

    fun getFullPoints(employee: String){
        if (employee.isEmpty()){ pointsFullList.value = repository.fullPoints() }
        else { pointsFullList.value = repository.fullPointsToName(employee, "") }
    }

    fun getFullEmployee(){
        employeeFullList.value = repositoryEmployee.consultFullEmployee()
    }

}