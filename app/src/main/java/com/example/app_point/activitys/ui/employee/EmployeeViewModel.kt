package com.example.app_point.activitys.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.Employee
import com.example.app_point.entity.PointsHours
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CalculateHours

class EmployeeViewModel(private var employee: RepositoryEmployee,
                        private var points: RepositoryPoint,
                        private val converterHours: CalculateHours) : ViewModel() {

    private val mEmployeeFullList = MutableLiveData<ArrayList<Employee>>()
    val employeeFullList: LiveData<ArrayList<Employee>> = mEmployeeFullList

    fun getFullEmployee(){
        mEmployeeFullList.value = employee.consultEmployee()
    }

    fun removeEmployee(id: Int): Boolean{
        return employee.removeEmployee(id)
    }

    fun removePoints(id: Int): Boolean{
        return points.removePoints(id)
    }

    fun consultPoint(id: Int, data: String): PointsHours?{
        return points.selectPoint(id, data)
    }

    fun editPoint(id: Int, data: String, positionHour: Int, hour: String): Boolean{
        val hourInt = converterHours.converterHoursInMinutes(hour)
        return points.setPointByDate(id, data, positionHour, hour, hourInt)
    }
}