package com.example.app_point.fragments.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.repository.ReposiitoryEmployee

class RegisterViewModel(application: Application):  AndroidViewModel(application) {

    private val mRepositoryEmployee: ReposiitoryEmployee = ReposiitoryEmployee(application)

    private val mEmployeeList = MutableLiveData<List<String>>()
    val textEmployee: LiveData<List<String>> = mEmployeeList

    private val mOfficeList = MutableLiveData<List<String>>()
    val textOffice: LiveData<List<String>> = mOfficeList

    fun getEmployeeList(){
        mEmployeeList.value = mRepositoryEmployee.getEmployeeList()
    }
    fun getOfficeList(){
        mOfficeList.value = mRepositoryEmployee.getOfficeList()
    }
}