package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint

class ViewModelPoints (application: Application): AndroidViewModel(application) {

    private var mSearchRecycler: RepositoryPoint = RepositoryPoint (application)

    private val mEmployeeList = MutableLiveData<String>()
    val employeeList: LiveData<String> = mEmployeeList

    private val mDataList = MutableLiveData<List<String>>()
    val dataList: LiveData<List<String>> = mDataList

    private val mHoraList = MutableLiveData<List<String>>()
    val horaList: LiveData<List<String>> = mHoraList

    private val mDateSelected = MutableLiveData<List<String>>()
    val dateSelected: LiveData<List<String>> = mDateSelected

    private val mHourSelected = MutableLiveData<List<String>>()
    val hourSelected: LiveData<List<String>> = mHourSelected

    fun getEmployee(employee: String){
        mEmployeeList.value = employee
    }
    fun getData(employee: String){
        mDataList.value = mSearchRecycler.storeSelectDate(employee)
    }
    fun getHora(employee: String){
        mHoraList.value = mSearchRecycler.storeSelectHours(employee)
    }
    fun getDateAndHourSelected(name: String, date: String){
        mDateSelected.value = mSearchRecycler.storeSelectNameDate(name, date)
        mHourSelected.value = mSearchRecycler.storeSelectNameHours(name, date)
    }
}