package com.example.app_point.activitys.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.entity.*
import com.example.app_point.interfaces.RepositoryData

class ProfileViewModel(private val searchRecycler: RepositoryData) : ViewModel() {

    private val hoursExtrasAndDone = MutableLiveData<ExtraDoneEntity?>()
    val hoursExtras: LiveData<ExtraDoneEntity?> = hoursExtrasAndDone

    private val pointsFullList = MutableLiveData<PointsHours?>()
    val pointsList: LiveData<PointsHours?> = pointsFullList

    private val statusEmployee = MutableLiveData<String>()
    val statusCurrent: LiveData<String> = statusEmployee

    fun getFullPoints(id: Int, date: String){
        pointsFullList.value = searchRecycler.selectPoint(id, date)
    }

    fun modifyStatusEmployee(employee: EmployeeEntityFull, status: String){

        statusEmployee.value = when {
            searchRecycler.modifyStatusEmployee(employee.id, status) -> {
                "${employee.nameEmployee} agora está $status!"
            }
            else -> "Ocoorreu um erro, tente novamente!"
        }
    }

    fun consultExtrasAndDone(id: Int){
        hoursExtrasAndDone.value = searchRecycler.consultTotalExtraByIdEmployee(id)
    }

}