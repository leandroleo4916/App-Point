package com.example.app_point.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.repository.RepositoryPoint

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepositoryPoint: RepositoryPoint = RepositoryPoint(application)

    private val mListEmployee = MutableLiveData<List<String>>()
    val listEmployee: LiveData<List<String>> = mListEmployee

    private val mListHours = MutableLiveData<List<String>>()
    val listHours: LiveData<List<String>> = mListHours

    private val mListDate = MutableLiveData<List<String>>()
    val listDate: LiveData<List<String>> = mListDate

    fun getEmployeeList(){
        mListEmployee.value = mRepositoryPoint.storePointEmployee()
    }
    fun getHourList(){
        mListHours.value = mRepositoryPoint.storePointHour()
    }
    fun getDateList(){
        mListDate.value = mRepositoryPoint.storePointDate()
    }
}