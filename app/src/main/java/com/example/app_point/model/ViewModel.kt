package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.repository.RepositoryPoint

class ViewModel (application: Application): AndroidViewModel(application) {

    private var mBuscarRecycler: RepositoryPoint = RepositoryPoint (application)

    private val mFuncionarioList = MutableLiveData<List<String>>()
    val employeeList: LiveData<List<String>> = mFuncionarioList

    private val mDataList = MutableLiveData<List<String>>()
    val dataList: LiveData<List<String>> = mDataList

    private val mHoraList = MutableLiveData<List<String>>()
    val horaList: LiveData<List<String>> = mHoraList

    fun getEmployee(){
        mFuncionarioList.value = mBuscarRecycler.storePointEmployee()
    }
    fun getData(){
        mDataList.value = mBuscarRecycler.storePointDate()
    }
    fun getHora(){
        mHoraList.value = mBuscarRecycler.storePointHour()
    }
}