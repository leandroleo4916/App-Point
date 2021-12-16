package com.example.app_point.activitys.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app_point.utils.CalculateHours
import com.example.app_point.entity.*
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.repository.RepositoryPoint
import com.example.app_point.utils.CaptureDateCurrent

class DashboardViewModel (private var points: RepositoryPoint,
                          private var employee: RepositoryEmployee,
                          private val captureDateCurrent: CaptureDateCurrent,
                          private val calculateHours: CalculateHours,
                          private val adapterDashboardDetail: AdapterDashboardDetail,
                          private val adapterDashboardRanking: AdapterDashboardRanking): ViewModel() {

    private val totalEmployeeList = MutableLiveData<Int>()
    val employeeTotal: LiveData<Int> = totalEmployeeList

    private val totalEmployeeVacation = MutableLiveData<Int>()
    val employeeVacation: LiveData<Int> = totalEmployeeVacation

    private val pointsFullList = MutableLiveData<Int>()
    val pointsList: LiveData<Int> = pointsFullList

    fun consultEmployeeAndPoints(){

        val date = captureDateCurrent.captureDate()
        val employee = employee.consultFullEmployee()
        val listEmployeeAndHours: ArrayList<EntityDashboard> = arrayListOf()
        val bestEmployee: ArrayList<EntityDashboard> = arrayListOf()

        for (i in employee){
            val points = consultPoint(i.nameEmployee, date)
            val hours = calculateHours.calculationHours(i.workload, points)
            listEmployeeAndHours.add(EntityDashboard(i.photo, hours.extraHour, hours.horasHour))
        }
        adapterRanking(listEmployeeAndHours)
        adapterDashboardDetail.updateHour(listEmployeeAndHours)
        totalEmployeeList.value = employee.size
    }

    private fun adapterRanking(listHours: ArrayList<EntityDashboard>) {
        adapterDashboardRanking.updateHour(listHours)
    }

    private fun consultPoint(name: String, date: String): ArrayList<PointsEntity?>{
        return points.fullPointsToName(name, date)
    }
}