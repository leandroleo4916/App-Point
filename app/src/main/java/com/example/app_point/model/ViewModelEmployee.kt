package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class ViewModelEmployee (application: Application,
                         private var employee: RepositoryEmployee,
                         private var points: RepositoryPoint)
                         : AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<ArrayList<EmployeeEntity>>()
    val employeeFullList: LiveData<ArrayList<EmployeeEntity>> = mEmployeeFullList

    fun getFullEmployee(){
        mEmployeeFullList.value = employee.consultFullEmployee()
    }

    fun removeEmployee(name: String): Boolean{
        return employee.removeEmployee(name)
    }

    fun removePoints(name: String): Boolean{
        return points.removePoints(name)
    }

}