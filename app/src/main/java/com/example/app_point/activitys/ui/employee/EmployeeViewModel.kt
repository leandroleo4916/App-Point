package com.example.app_point.activitys.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.utils.CalculateHours
import com.example.app_point.entity.Employee
import com.example.app_point.entity.HourEntityInt
import com.example.app_point.entity.PointsHours
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

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

    fun removePoints(name: String): Boolean{
        return points.removePoints(name)
    }

    fun consultPoint(name: String, data: String): PointsHours?{
        return points.selectPoint(name, data)
    }

    fun consultPointEdit(name: String, data: String) {

        val hour = points.selectPointInt(name, data)

        if (hour?.hora1 != null && hour.hora2 != null && hour.hora3 != null && hour.hora4 != null){

            val consultExtra = employee.consultTime(name)
            val extra = consultExtra?.let { converterHours.calculateHoursExtras(it, HourEntityInt(hour.hora1, hour.hora2, hour.hora3, hour.hora4)) }
            points.setPointExtra(name, data, extra!!)
        }
    }

    fun editPoint(name: String, data: String, positionHour: Int, hour: String): Boolean{
        val hourInt = converterHours.converterHoursInMinutes(hour)
        return points.setPointByDate(name, data, positionHour, hour, hourInt)
    }
}