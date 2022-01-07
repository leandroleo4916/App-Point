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
                          private val calculateHours: CalculateHours): ViewModel() {

    private val totalEmployeeList = MutableLiveData<Int>()
    val employeeTotal: LiveData<Int> = totalEmployeeList

    private val totalEmployeeVacation = MutableLiveData<Int>()
    val employeeVacation: LiveData<Int> = totalEmployeeVacation

    private val totalEmployeeActive = MutableLiveData<Int>()
    val employeeActive: LiveData<Int> = totalEmployeeActive

    private val totalDetailEmployee = MutableLiveData<ArrayList<EntityDashboard>>()
    val employeeDetail: LiveData<ArrayList<EntityDashboard>> = totalDetailEmployee

    private val totalBestEmployee = MutableLiveData<ArrayList<EntityBestEmployee>>()
    val employeeBest: LiveData<ArrayList<EntityBestEmployee>> = totalBestEmployee

    fun consultEmployeeAndPoints(){

        val employee = employee.consultFullEmployee()
        val listEmployeeHoursExtraAndDone: ArrayList<EntityDashboard> = arrayListOf()
        val bestEmployee: ArrayList<EntityBestEmployee> = arrayListOf()
        val vacation = 0
        val active = 0

        for (i in employee){

            var dateFirst = captureDateCurrent.captureDateFirst()
            val dateCurrent = captureDateCurrent.captureDateCurrent()
            val dateTomorrow = captureDateCurrent.captureNextDate(dateCurrent)
            var hExtra = 0
            var hDone = 0
            var punctuation = 0

            while (dateFirst != dateTomorrow){

                val point = consultPoint(i.id, dateFirst)
                val hourDone = calculateHours.calculateHoursDone(point)

                when {
                    point == null -> {
                        hExtra += 0
                        punctuation += 0
                    }
                    point.extra == null -> {
                        punctuation += 0
                    }
                    point.punctuation == null -> {
                        punctuation += 0
                    }
                    else -> {
                        hExtra += point.extra
                        punctuation += point.punctuation
                    }
                }

                hDone += hourDone
                dateFirst = captureDateCurrent.captureNextDate(dateFirst)
            }

            val extraString = calculateHours.convertMinutesInHours(hExtra)
            val doneString = calculateHours.convertMinutesInHours(hDone)

            listEmployeeHoursExtraAndDone.add(EntityDashboard(i.photo, extraString, doneString))
            bestEmployee.add(EntityBestEmployee(i.photo, punctuation))

            //if (i.vacation == 1) vacation++
            //if (i.active == 1) active++
        }

        totalEmployeeList.value = employee.size
        totalEmployeeVacation.value = vacation
        totalEmployeeActive.value = active
        totalDetailEmployee.value = listEmployeeHoursExtraAndDone
        totalBestEmployee.value = bestEmployee
    }

    private fun consultPoint(id: Int, date: String): HourEntityInt? {
        return points.selectPointInt(id, date)
    }
}