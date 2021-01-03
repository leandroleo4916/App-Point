package com.example.app_point.fragments.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.repository.ReposiitoryEmployee

class RegisterViewModel(application: Application):  AndroidViewModel(application) {

    private val mReposiitoryEmployee: ReposiitoryEmployee = ReposiitoryEmployee(application)

    private val mEmployeeList = MutableLiveData<List<String>>()
    val textEmployee: LiveData<List<String>> = mEmployeeList

    private val mOfficeList = MutableLiveData<List<String>>()
    val textOffice: LiveData<List<String>> = mOfficeList

    private val mEmailList = MutableLiveData<List<String>>()
    val textEmail: LiveData<List<String>> = mEmailList

    private val mPhoneList = MutableLiveData<List<String>>()
    val textPhone: LiveData<List<String>> = mPhoneList

    private val mAdmissionList = MutableLiveData<List<String>>()
    val textAdmission: LiveData<List<String>> = mAdmissionList

    private val mHour1List = MutableLiveData<List<String>>()
    val textHour1: LiveData<List<String>> = mHour1List

    private val mHour2List = MutableLiveData<List<String>>()
    val textHour2: LiveData<List<String>> = mHour2List

    private val mHour3List = MutableLiveData<List<String>>()
    val textHour3: LiveData<List<String>> = mHour3List

    private val mHour4List = MutableLiveData<List<String>>()
    val textHour4: LiveData<List<String>> = mHour4List

    fun getEmployeeList(){
        mEmployeeList.value = mReposiitoryEmployee.getEmployeeList()
    }
    fun getOfficeList(){
        mOfficeList.value = mReposiitoryEmployee.getOfficeList()
    }
    fun getEmailList(){
        mEmailList.value = mReposiitoryEmployee.getEmailList()
    }
    fun getPhoneList(){
        mPhoneList.value = mReposiitoryEmployee.getPhoneList()
    }
    fun getAdmissionList(){
        mAdmissionList.value = mReposiitoryEmployee.getAdmissionList()
    }
    fun getHour1List(){
        mHour1List.value = mReposiitoryEmployee.getHour1List()
    }
    fun getHour2List(){
        mHour2List.value = mReposiitoryEmployee.getHour2List()
    }
    fun getHour3List(){
        mHour3List.value = mReposiitoryEmployee.getHour3List()
    }
    fun getHour4List(){
        mHour4List.value = mReposiitoryEmployee.getHour4List()
    }
}