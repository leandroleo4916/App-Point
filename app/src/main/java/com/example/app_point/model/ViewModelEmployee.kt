package com.example.app_point.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.app_point.entity.EmployeeDados
import com.example.app_point.repository.RepositoryDataEmployee
import com.google.firebase.firestore.QuerySnapshot

class ViewModelEmployee (application: Application): AndroidViewModel(application) {

    private val mEmployeeFullList = MutableLiveData<QuerySnapshot>()
    val employeeFullList: LiveData<QuerySnapshot> = mEmployeeFullList

    fun getFullEmployee(result: QuerySnapshot) {
        mEmployeeFullList.value = result
    }

}