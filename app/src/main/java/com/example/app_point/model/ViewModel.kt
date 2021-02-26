package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class ViewModel (application: Application): AndroidViewModel(application) {

    private var mSearchRecycler: RepositoryPoint = RepositoryPoint (application)
    private val mDataEmployee: RepositoryEmployee = RepositoryEmployee(application)

    private val mEmployeeList = MutableLiveData<List<String>>()
    val employeeList: LiveData<List<String>> = mEmployeeList

    private val mDataList = MutableLiveData<List<String>>()
    val dataList: LiveData<List<String>> = mDataList

    private val mHoraList = MutableLiveData<List<String>>()
    val horaList: LiveData<List<String>> = mHoraList

    private val mEmployeeData = MutableLiveData<EmployeeEntity>()
    val employeeData: LiveData<EmployeeEntity> = mEmployeeData

    fun getEmployee(employee: String){
        if (employee == ""){
            mEmployeeList.value = mSearchRecycler.storePointEmployee()
        }else {
            mEmployeeList.value = mSearchRecycler.storeSelectName(employee)
        }
    }

    fun getData(employee: String){
        if (employee == ""){
            mDataList.value = mSearchRecycler.storePointDate()
        }else {
            mDataList.value = mSearchRecycler.storeSelectDate(employee)
        }
    }

    fun getHora(employee: String){
        if (employee == ""){
            mHoraList.value = mSearchRecycler.storePointHour()
        }else {
            mHoraList.value = mSearchRecycler.storeSelectHours(employee)
        }
    }

    fun getDataEmployee(employee: String){
        mEmployeeData.value = mDataEmployee.consultDataEmployee(employee)
    }
}