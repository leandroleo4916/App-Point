package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.Employee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.entity.PointsHours
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class ViewModelEmployee (application: Application, private var employee: RepositoryEmployee,
                         private var points: RepositoryPoint) : AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<ArrayList<Employee>>()
    val employeeFullList: LiveData<ArrayList<Employee>> = mEmployeeFullList

    fun getFullEmployee(){
        mEmployeeFullList.value = employee.consultDataEmployee()
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