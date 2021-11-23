package com.example.app_point.activitys.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.Employee
import com.example.app_point.entity.PointsHours
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class EmployeeViewModel(private var employee: RepositoryEmployee,
                        private var points: RepositoryPoint) : ViewModel() {

    private val mEmployeeFullList = MutableLiveData<ArrayList<Employee>>()
    val employeeFullList: LiveData<ArrayList<Employee>> = mEmployeeFullList

    fun getFullEmployee(){
        mEmployeeFullList.value = employee.consultEmployee()
    }

    fun removeEmployee(id: Int): Boolean{
        return employee.removeEmployee(id)
    }

    fun removePoints(name: String): Boolean{
        return points.removePoints(name)
    }

    fun consultPoint(name: String, data: String): PointsHours?{
        return points.selectPoint(name, data)
    }

    fun editPoint(name: String, data: String, positionHour: Int, hour: String): Boolean{
        return points.setPointByDate(name, data, positionHour, hour)
    }
}